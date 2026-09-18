package com.example.Url_Shortner;

import com.example.Url_Shortner.dto.UrlAnalyticsResponse;
import com.example.Url_Shortner.dto.UrlRequest;
import com.example.Url_Shortner.entity.UrlMapping;
import com.example.Url_Shortner.exception.AliasAlreadyExistsException;
import com.example.Url_Shortner.exception.InvalidUrlException;
import com.example.Url_Shortner.service.UrlService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UrlShortnerApplicationTests {

	@Autowired
	private UrlService urlService;

	@Test
	@DisplayName("Context loads successfully")
	void contextLoads() {
		assertNotNull(urlService);
	}

	@Test
	@DisplayName("Create short URL normalizes URL and generates unique 6-character short code")
	void testCreateShortUrl() {
		UrlRequest request = new UrlRequest();
		request.setOriginalUrl("example.com/page");

		UrlMapping mapping = urlService.createShortUrl(request);
		assertNotNull(mapping);
		assertNotNull(mapping.getShortCode());
		assertEquals(6, mapping.getShortCode().length());
		assertEquals("https://example.com/page", mapping.getOriginalUrl());
	}

	@Test
	@DisplayName("Create short URL with custom alias succeeds and handles collisions")
	void testCustomAliasFlow() {
		UrlRequest req1 = new UrlRequest();
		req1.setOriginalUrl("https://vit.ac.in");
		req1.setCustomAlias("vit-custom-code");

		UrlMapping mapping1 = urlService.createShortUrl(req1);
		assertEquals("vit-custom-code", mapping1.getShortCode());

		// Re-using same alias must throw AliasAlreadyExistsException
		UrlRequest req2 = new UrlRequest();
		req2.setOriginalUrl("https://google.com");
		req2.setCustomAlias("vit-custom-code");

		assertThrows(AliasAlreadyExistsException.class, () -> urlService.createShortUrl(req2));
	}

	@Test
	@DisplayName("Empty or blank URL throws InvalidUrlException")
	void testInvalidUrlValidation() {
		UrlRequest request = new UrlRequest();
		request.setOriginalUrl("   ");

		assertThrows(InvalidUrlException.class, () -> urlService.createShortUrl(request));
	}

	@Test
	@DisplayName("Resolving shortCode returns original URL and increments click count")
	void testGetOriginalUrlAndClickCount() {
		UrlRequest request = new UrlRequest();
		request.setOriginalUrl("https://spring.io");
		request.setCustomAlias("spring-home");

		urlService.createShortUrl(request);

		// Resolve 2 times
		String url1 = urlService.getOriginalUrl("spring-home");
		String url2 = urlService.getOriginalUrl("spring-home");

		assertEquals("https://spring.io", url1);
		assertEquals("https://spring.io", url2);

		UrlAnalyticsResponse analytics = urlService.getAnalytics("spring-home", "http://localhost:8080/api");
		assertEquals(2, analytics.getClickCount());
	}

	@Test
	@DisplayName("QR Code generation returns non-empty byte array")
	void testQrCodeGeneration() {
		byte[] qrBytes = urlService.generateQrCode("https://localhost:8080/api/abc123", 200, 200);
		assertNotNull(qrBytes);
		assertTrue(qrBytes.length > 0);
	}
}
