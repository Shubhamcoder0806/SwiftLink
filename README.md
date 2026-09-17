# 🔗 URL Shortener API

A RESTful URL Shortener service built using Spring Boot that converts long URLs into compact, shareable links and redirects users to the original destination.

## 📖 Overview

This project provides a scalable URL shortening solution using a layered Spring Boot architecture. Users can submit long URLs and receive a unique short URL that can be used for redirection.

## 🚀 Features

- Shorten long URLs
- Automatic short code generation
- Redirect to original URL
- REST API architecture
- Exception handling
- DTO-based request/response handling
- Layered architecture following Spring Boot best practices
- Database persistence

## 🏗️ Architecture

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Database
```

## 🛠️ Tech Stack

- Java 17+
- Spring Boot
- Spring Data JPA
- Maven
- MySQL / PostgreSQL (depending on configuration)
- REST APIs

## 📂 Project Structure

```text
src/main/java/com/example/Url_Shortner

├── controller
├── dto
├── entity
├── exception
├── repository
├── service
└── UrlShortnerApplication.java
```

## ⚙️ Installation

### Clone Repository

```bash
git clone https://github.com/Shubhamcoder0806/Url_Shortner.git
cd Url_Shortner
```

### Configure Database

Update your `application.properties`:

```properties
spring.datasource.url=YOUR_DATABASE_URL
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
```

### Run Project

```bash
./mvnw spring-boot:run
```

Or

```bash
mvn spring-boot:run
```

## 📡 API Endpoints

### Create Short URL

```http
POST /api/shorten
```

Request:

```json
{
  "url": "https://www.example.com"
}
```

Response:

```json
{
  "shortUrl": "http://localhost:8080/abc123"
}
```

### Redirect URL

```http
GET /{shortCode}
```

Redirects users to the original URL.

## 🎯 Learning Outcomes

- Spring Boot Fundamentals
- REST API Development
- DTO Pattern
- JPA & Database Integration
- Exception Handling
- Layered Architecture Design

## 🔮 Future Improvements

- User Authentication
- URL Analytics
- QR Code Generation
- Custom Short URLs
- Click Tracking Dashboard
- URL Expiration Support

## 👨‍💻 Author

**Shubham Mishra**

GitHub: https://github.com/Shubhamcoder0806

## ⭐ Star the Repository

If you found this project useful, consider giving it a star.
