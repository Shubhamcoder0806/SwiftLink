package com.example.Url_Shortner.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UrlRequest {

    @NotBlank(message = "Original URL cannot be null, empty, or blank")
    @Size(max = 2048, message = "URL length cannot exceed 2048 characters")
    private String originalUrl;

    @Pattern(regexp = "^[a-zA-Z0-9_-]{3,20}$", message = "Custom alias must be 3-20 alphanumeric characters (hyphens and underscores allowed)")
    private String customAlias;

    private Integer expirationDays;

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getCustomAlias() {
        return customAlias;
    }

    public void setCustomAlias(String customAlias) {
        this.customAlias = customAlias;
    }

    public Integer getExpirationDays() {
        return expirationDays;
    }

    public void setExpirationDays(Integer expirationDays) {
        this.expirationDays = expirationDays;
    }
}
