package com.example.Url_Shortner.controller;
import com.example.Url_Shortner.dto.UrlResponse;
import com.example.Url_Shortner.dto.UrlRequest;
import com.example.Url_Shortner.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.ServerRequest;

import java.net.URI;

@RestController
@RequestMapping("/api")
public class Url_Controller {

    @Autowired
    private UrlService urlService;

    @PostMapping("/shorten")
    public ResponseEntity<UrlResponse> shorten(@RequestBody UrlRequest request){
        String shortCode;
        shortCode = urlService.createShortUrl(request.getOriginalUrl());
        String shortUrl =  "https://localhost:8000/" + shortCode;

        UrlResponse response = new UrlResponse(shortUrl,request.getOriginalUrl());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode){
        String originalUrl = urlService.getOriginalUrl(shortCode);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(originalUrl));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);

    }


}
