package com.example.Url_Shortner.entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "UrlMapping")

public class UrlMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    @Column(unique = true , nullable = false , length = 10)
    private String shortCode;

    @Column(nullable = false, length = 2048)
    private String originalUrl;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private int clickCount = 0 ;

    public Long getId() {return id;}
    public void setId(Long id) { this.id = id;}

    public String getShortCode() {return shortCode;}
    public void setShortCode(String shortCode){this.shortCode = shortCode;}

    public String getOriginalUrl() {return originalUrl;}
    public void setOriginalUrl(String originalUrl) {this.originalUrl = originalUrl;}

    public LocalDateTime getCreatedAt() {return createdAt;}
    public void setCreatedAt(LocalDateTime createdAt) {this.createdAt = createdAt;}

    public int getClickCount() {
        return clickCount;
    }
    public void setClickCount(int clickCount) {
        this.clickCount = clickCount;
    }
}
