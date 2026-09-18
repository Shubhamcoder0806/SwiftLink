package com.example.Url_Shortner.controller;

import com.example.Url_Shortner.dto.UrlAnalyticsResponse;
import com.example.Url_Shortner.dto.UrlRequest;
import com.example.Url_Shortner.dto.UrlResponse;
import com.example.Url_Shortner.entity.UrlMapping;
import com.example.Url_Shortner.service.UrlService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api")
public class Url_Controller {

    @Autowired
    private UrlService urlService;

    @PostMapping("/shorten")
    public ResponseEntity<UrlResponse> shorten(@Valid @RequestBody UrlRequest request, HttpServletRequest httpRequest) {
        UrlMapping mapping = urlService.createShortUrl(request);

        String baseUrl = getBaseUrl(httpRequest);
        String fullShortUrl = baseUrl + "/api/" + mapping.getShortCode();
        String qrCodeUrl = baseUrl + "/api/qr/" + mapping.getShortCode();

        UrlResponse response = new UrlResponse(
                fullShortUrl,
                mapping.getOriginalUrl(),
                mapping.getShortCode(),
                mapping.getCreatedAt(),
                mapping.getExpiresAt(),
                qrCodeUrl
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode) {
        String originalUrl = urlService.getOriginalUrl(shortCode);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(originalUrl));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @GetMapping("/analytics/{shortCode}")
    public ResponseEntity<UrlAnalyticsResponse> getAnalytics(@PathVariable String shortCode, HttpServletRequest httpRequest) {
        String baseUrl = getBaseUrl(httpRequest) + "/api";
        UrlAnalyticsResponse response = urlService.getAnalytics(shortCode, baseUrl);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/qr/{shortCode}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getQrCode(@PathVariable String shortCode, HttpServletRequest httpRequest) {
        String baseUrl = getBaseUrl(httpRequest);
        String fullShortUrl = baseUrl + "/api/" + shortCode;

        byte[] qrImage = urlService.generateQrCode(fullShortUrl, 250, 250);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(qrImage);
    }

    private String getBaseUrl(HttpServletRequest request) {
        return ServletUriComponentsBuilder.fromContextPath(request)
                .build()
                .toUriString();
    }
}
