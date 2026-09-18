package com.example.Url_Shortner.service;

import com.example.Url_Shortner.dto.UrlAnalyticsResponse;
import com.example.Url_Shortner.dto.UrlRequest;
import com.example.Url_Shortner.entity.UrlMapping;
import com.example.Url_Shortner.exception.AliasAlreadyExistsException;
import com.example.Url_Shortner.exception.InvalidUrlException;
import com.example.Url_Shortner.exception.UrlExpiredException;
import com.example.Url_Shortner.exception.UrlNotFoundException;
import com.example.Url_Shortner.repository.UrlMappingRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class UrlService {

    private static final Logger logger = LoggerFactory.getLogger(UrlService.class);
    private static final String BASE62 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom random = new SecureRandom();
    private static final String REDIS_PREFIX = "url:";

    @Autowired
    private UrlMappingRepository repository;

    @Autowired(required = false)
    private RedisTemplate<String, String> redisTemplate;

    /**
     * Shortens a given URL with optional custom alias and expiration days.
     */
    @Transactional
    public UrlMapping createShortUrl(UrlRequest request) {
        String normalizedUrl = normalizeAndValidateUrl(request.getOriginalUrl());

        String shortCode;
        if (request.getCustomAlias() != null && !request.getCustomAlias().trim().isEmpty()) {
            shortCode = request.getCustomAlias().trim();
            if (repository.existsByShortCode(shortCode)) {
                throw new AliasAlreadyExistsException("Custom alias '" + shortCode + "' is already in use.");
            }
        } else {
            shortCode = generateUniqueShortCode();
        }

        LocalDateTime expiresAt = null;
        if (request.getExpirationDays() != null && request.getExpirationDays() > 0) {
            expiresAt = LocalDateTime.now().plusDays(request.getExpirationDays());
        }

        UrlMapping mapping = new UrlMapping(shortCode, normalizedUrl, expiresAt);
        UrlMapping savedMapping = repository.save(mapping);

        // Populate Redis Cache
        cacheOriginalUrl(shortCode, normalizedUrl, expiresAt);

        return savedMapping;
    }

    /**
     * Resolves shortCode to originalUrl with Redis caching and click tracking.
     */
    @Transactional
    public String getOriginalUrl(String shortCode) {
        if (shortCode == null || shortCode.trim().isEmpty()) {
            throw new InvalidUrlException("Short code cannot be empty");
        }

        String cleanCode = shortCode.trim();

        // 1. Check Redis Cache
        String cachedUrl = getFromRedisCache(cleanCode);
        if (cachedUrl != null) {
            // Asynchronously or background update click count in DB
            incrementClickCountInDb(cleanCode);
            return cachedUrl;
        }

        // 2. Cache Miss: Query Database
        UrlMapping mapping = repository.findByShortCode(cleanCode)
                .orElseThrow(() -> new UrlNotFoundException("Short URL not found for code: " + cleanCode));

        // 3. Check Expiration
        if (mapping.getExpiresAt() != null && LocalDateTime.now().isAfter(mapping.getExpiresAt())) {
            throw new UrlExpiredException("The short link '" + cleanCode + "' has expired.");
        }

        // 4. Increment click count
        mapping.setClickCount(mapping.getClickCount() + 1);
        repository.save(mapping);

        // 5. Populate Redis Cache for subsequent requests
        cacheOriginalUrl(cleanCode, mapping.getOriginalUrl(), mapping.getExpiresAt());

        return mapping.getOriginalUrl();
    }

    /**
     * Gets analytics data for a given shortCode.
     */
    public UrlAnalyticsResponse getAnalytics(String shortCode, String baseUrl) {
        String cleanCode = shortCode != null ? shortCode.trim() : "";
        UrlMapping mapping = repository.findByShortCode(cleanCode)
                .orElseThrow(() -> new UrlNotFoundException("Short URL not found for code: " + cleanCode));

        boolean isExpired = mapping.getExpiresAt() != null && LocalDateTime.now().isAfter(mapping.getExpiresAt());
        String fullShortUrl = baseUrl + "/" + mapping.getShortCode();

        return new UrlAnalyticsResponse(
                mapping.getShortCode(),
                mapping.getOriginalUrl(),
                fullShortUrl,
                mapping.getClickCount(),
                mapping.getCreatedAt(),
                mapping.getExpiresAt(),
                isExpired
        );
    }

    /**
     * Generates a PNG QR code for a given short URL.
     */
    public byte[] generateQrCode(String text, int width, int height) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            return pngOutputStream.toByteArray();
        } catch (Exception e) {
            logger.error("Failed to generate QR code", e);
            throw new RuntimeException("Error generating QR Code: " + e.getMessage());
        }
    }

    private String normalizeAndValidateUrl(String originalUrl) {
        if (originalUrl == null || originalUrl.trim().isEmpty()) {
            throw new InvalidUrlException("URL cannot be null or empty");
        }

        String trimmed = originalUrl.trim();

        // Disallow dangerous URI schemes
        String lower = trimmed.toLowerCase();
        if (lower.startsWith("javascript:") || lower.startsWith("data:") || lower.startsWith("file:")) {
            throw new InvalidUrlException("Invalid URL scheme provided");
        }

        // Add default https scheme if missing
        if (!lower.startsWith("http://") && !lower.startsWith("https://")) {
            trimmed = "https://" + trimmed;
        }

        try {
            URI uri = new URI(trimmed);
            if (uri.getHost() == null || uri.getHost().trim().isEmpty()) {
                throw new InvalidUrlException("Invalid URL host structure: " + originalUrl);
            }
            return trimmed;
        } catch (Exception e) {
            throw new InvalidUrlException("Malformed URL: " + originalUrl);
        }
    }

    private String generateUniqueShortCode() {
        String code;
        int attempts = 0;
        do {
            StringBuilder sb = new StringBuilder(6);
            for (int i = 0; i < 6; i++) {
                sb.append(BASE62.charAt(random.nextInt(BASE62.length())));
            }
            code = sb.toString();
            attempts++;
            if (attempts > 20) {
                // Expand length if collision probability increases
                code = code + BASE62.charAt(random.nextInt(BASE62.length()));
            }
        } while (repository.existsByShortCode(code));
        return code;
    }

    private void cacheOriginalUrl(String shortCode, String originalUrl, LocalDateTime expiresAt) {
        if (redisTemplate == null) return;
        try {
            String redisKey = REDIS_PREFIX + shortCode;
            if (expiresAt != null) {
                long secondsUntilExpiry = Duration.between(LocalDateTime.now(), expiresAt).getSeconds();
                if (secondsUntilExpiry > 0) {
                    redisTemplate.opsForValue().set(redisKey, originalUrl, Duration.ofSeconds(secondsUntilExpiry));
                }
            } else {
                // Default 24h TTL for high performance cache
                redisTemplate.opsForValue().set(redisKey, originalUrl, Duration.ofHours(24));
            }
        } catch (Exception e) {
            logger.warn("Redis unavailable for caching shortCode {}: {}", shortCode, e.getMessage());
        }
    }

    private String getFromRedisCache(String shortCode) {
        if (redisTemplate == null) return null;
        try {
            String redisKey = REDIS_PREFIX + shortCode;
            return redisTemplate.opsForValue().get(redisKey);
        } catch (Exception e) {
            logger.warn("Redis lookup failed for shortCode {}: {}", shortCode, e.getMessage());
            return null;
        }
    }

    private void incrementClickCountInDb(String shortCode) {
        try {
            repository.findByShortCode(shortCode).ifPresent(mapping -> {
                mapping.setClickCount(mapping.getClickCount() + 1);
                repository.save(mapping);
            });
        } catch (Exception e) {
            logger.error("Failed to update click count for {}: {}", shortCode, e.getMessage());
        }
    }
}
