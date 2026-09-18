# VIT BHOPAL UNIVERSITY
**Kothrikalan, Sehore, Madhya Pradesh**

---

## PROJECT REPORT

# URL SHORTENER & ANALYTICS SERVICE
### (SwiftLink)

*A Web-Based Java Spring Boot Application with Redis Caching*

Submitted in partial fulfilment of the requirements of the course
### **PROGRAMMING IN JAVA**

| Attribute | Details |
|---|---|
| **Submitted by** | **Shubham Mishra** |
| **Registration Number** | **24BCY10064** |
| **Programme** | **B.Tech CSE (Cyber Security and Digital forensics)** |
| **Course** | **Programming in Java** |
| **University** | **VIT Bhopal University** |
| **Academic Year** | **2025 – 2026** |

---

<div page-break="true"></div>

## DECLARATION

I, **Shubham Mishra**, bearing Registration Number **24BCY10064**, a student of **B.Tech in Computer Science and Engineering (Cyber Security and Digital forensics)** at **VIT Bhopal University**, hereby declare that the project report entitled **“URL Shortener & Analytics Service (SwiftLink)”** is an original piece of work carried out by me as part of the course **Programming in Java**.

The application described in this report is a RESTful web application designed, implemented, and tested by me. The concepts, design decisions, and source code presented here are the outcome of my own study of the Java programming language, Spring Boot framework, Redis caching engine, relational database management systems, and exception-handling mechanisms. Wherever material has been consulted from documentation or reference sources, due acknowledgement has been made in the References section of this report.

I further declare that this report has not been submitted previously, in whole or in part, for the award of any other degree, diploma, or certification. The complete source code of the project is maintained under version control and is publicly available in the GitHub repository cited at the end of this report.

<br>

**Place:** Bhopal, Madhya Pradesh  
**Date:** 18th September 2026  

<div align="right">
  <b>Shubham Mishra</b><br>
  Reg. No. 24BCY10064<br>
  B.Tech (CSE), VIT Bhopal University
</div>

---

<div page-break="true"></div>

## ACKNOWLEDGEMENT

The completion of this project has been possible because of the guidance and support extended to me by several people, and I take this opportunity to record my sincere gratitude to them.

I express my thanks to the faculty of the course **Programming in Java** at **VIT Bhopal University** for the structured instruction that introduced me to object-oriented design, layered architecture, data access patterns, and robust exception handling. The laboratory sessions, in particular, shaped the way this application was organised into clean layers and helped me appreciate why a clear separation between the presentation, service, data access, and caching layers matters in real-world software.

I am grateful to the **School of Computing Science and Engineering (SCSE)** for providing the academic environment and laboratory infrastructure required to develop and test this application.

I also wish to thank my classmates, who tested the web interface and reported edge cases that I had not anticipated, and my family, whose constant encouragement supported me throughout the development of this project.

Finally, I acknowledge the wider open-source developer community whose documentation on Java, Spring Boot, Redis, and Git served as a valuable reference during implementation.

<br>

<div align="right">
  <b>Shubham Mishra</b><br>
  24BCY10064<br>
  B.Tech (CSE), VIT Bhopal University
</div>

---

<div page-break="true"></div>

## ABSTRACT

Modern web applications constantly generate long, unwieldy URLs for deep links, resources, and tracking parameters. Sharing these long links in messages, publications, or mobile interfaces is prone to errors, truncation, and poor visual presentation. The **URL Shortener & Analytics Service (SwiftLink)** is a high-performance web application written in Java using Spring Boot and Redis, designed to compress lengthy URLs into unique, 6-character short keys, while tracking click metrics, offering custom aliases, link expiration, and generating QR codes.

To handle high traffic, the service incorporates **Redis caching**, ensuring that redirect lookups execute in sub-milliseconds without performing unnecessary SQL database roundtrips. The system features defensive input validation (`jakarta.validation`), automatic protocol normalization (prepending `https://`), security checks against malicious URI schemes (`javascript:`, `data:`, `file:`), and structured JSON exception handling.

The backend is built using a layered architecture comprising controllers, DTOs, services, entities, repositories, and custom configuration modules. Persistence is supported via an in-memory H2 database for zero-config local testing, alongside MySQL, MariaDB, and PostgreSQL configurations, supported by a production Docker Compose setup. The frontend features a responsive, dark-mode glassmorphism interface with copy-to-clipboard toast notifications, QR code preview/download, and link analytics drawers.

**Keywords:** Java, Spring Boot, Redis Caching, REST API, URL Shortener, Glassmorphism UI, QR Code, Input Validation, H2 Database, PostgreSQL, Docker, Object-Oriented Programming, Git, GitHub.

---

<div page-break="true"></div>

## TABLE OF CONTENTS

- **1. INTRODUCTION**
  - 1.1 Background
  - 1.2 Problem Statement
  - 1.3 Need for the Project
  - 1.4 Overview of URL Shortener & Analytics Service
- **2. OBJECTIVES OF THE PROJECT**
  - 2.1 Primary Objectives
  - 2.2 Secondary Objectives
- **3. SCOPE OF THE PROJECT**
  - 3.1 Scope Included in the Present Version
  - 3.2 Scope Deliberately Excluded
- **4. TECHNOLOGIES AND TOOLS USED**
  - 4.1 Justification of the Choices
- **5. SYSTEM REQUIREMENTS**
  - 5.1 Hardware Requirements
  - 5.2 Software Requirements
- **6. SYSTEM DESIGN**
  - 6.1 Overall Architecture
  - 6.2 Module Structure and Package Organisation
  - 6.3 Role of Each Component
  - 6.4 Data Flow
  - 6.5 Data Storage Format
- **7. PROJECT MODULES**
  - 7.1 URL Shortening & Normalization Module
  - 7.2 Redis Caching & Redirection Module
  - 7.3 Analytics & QR Code Generation Module
  - 7.4 Web Interface & Dashboard Module
  - 7.5 Validation & Exception Handling Module
- **8. OBJECT-ORIENTED PROGRAMMING CONCEPTS USED**
  - 8.1 Classes and Objects
  - 8.2 Encapsulation
  - 8.3 Abstraction
  - 8.4 Inheritance and Polymorphism
  - 8.5 Modularity
  - 8.6 Reusability
  - 8.7 Exception Handling
- **9. WORKING OF THE APPLICATION**
  - 9.1 Compilation and Execution
  - 9.2 Start-up Sequence
  - 9.3 Operating Cycle
  - 9.4 A Typical Session
- **10. SAMPLE INPUT AND OUTPUT**
  - 10.1 Shorten URL (Basic Request & Response)
  - 10.2 Custom Alias & Expiration Request
  - 10.3 Duplicate Custom Alias Rejection
  - 10.4 Invalid / Empty URL Validation Error
  - 10.5 Redirection Execution (HTTP 302)
  - 10.6 Click Analytics Output
  - 10.7 QR Code Image Output
- **11. TESTING**
- **12. ADVANTAGES**
- **13. LIMITATIONS**
- **14. FUTURE ENHANCEMENTS**
  - 14.1 User Authentication (JWT)
  - 14.2 Custom Domain Support
  - 14.3 Geo-location Analytics
  - 14.4 Distributed Rate Limiting
- **15. CONCLUSION**
- **16. REFERENCES**
- **17. GITHUB REPOSITORY**

---

<div page-break="true"></div>

## 1. INTRODUCTION

### 1.1 Background
The growth of modern web applications, social media platforms, and digital marketing has made web addresses increasingly complex, often containing lengthy query strings, session tokens, and tracking parameters. Long URLs are visually unappealing, difficult to read, easy to break when line-wrapped, and inefficient when shared across messaging platforms or printed collateral. A URL shortener maps a long web destination to a compact key, which redirects visitors seamlessly upon access.

### 1.2 Problem Statement
Basic URL shorteners implemented without caching or strict validation suffer from severe bottleneck and security issues:
- **Database Strain:** Resolving high-frequency short link redirects directly against SQL databases causes severe IOPS bottlenecks during traffic spikes.
- **Null & Empty Payload Crashes:** Inadequate validation allows blank or null URL requests to trigger uncaught exceptions (`NullPointerException`) or corrupt database entries.
- **Malicious & Broken Links:** Malformed URLs or dangerous URI schemes (e.g. `javascript:`, `file:`) can expose users to security risks.
- **Lack of Insights & Expiration:** Traditional simple scripts fail to track click analytics, link expiration, or mobile-friendly QR codes.

### 1.3 Need for the Project
This project addresses these operational challenges by designing and implementing a high-throughput, secure RESTful web application in Java. It combines **Spring Boot** with **Redis in-memory caching** for sub-millisecond lookups, **Jakarta Validation** for strict payload verification, **ZXing engine** for QR code generation, and **multi-database support** (H2, PostgreSQL, MySQL).

### 1.4 Overview of URL Shortener & Analytics Service (SwiftLink)
SwiftLink is a full-stack Java web application. When a user submits a long URL via the REST API or Web UI, the service normalizes the URL, checks for custom alias conflicts, generates a unique 6-character Base62/Random short key, saves the mapping to the database, and populates the Redis cache. When a visitor requests `GET /api/{shortCode}`, the system serves an instant HTTP 302 redirect from Redis, asynchronously updating the click count.

---

## 2. OBJECTIVES OF THE PROJECT

### 2.1 Primary Objectives
1. To design and implement a Spring Boot REST application capable of generating short codes and performing HTTP 302 redirects.
2. To integrate Redis in-memory caching for sub-millisecond link resolution.
3. To validate all user input (`@NotBlank`, `@Size`, `@Pattern`) and handle invalid data or empty strings gracefully without server crashes.
4. To implement custom aliases, link expiration policies, PNG QR code generation, and click analytics.
5. To provide multi-database support (H2 in-memory default, MySQL, PostgreSQL) and a containerized Docker Compose setup.
6. To design a responsive glassmorphism dark-mode web dashboard.

### 2.2 Secondary Objectives
1. To apply object-oriented principles (Encapsulation, Abstraction, Inheritance, Polymorphism, Modularity) across a Spring Boot architecture.
2. To structure the codebase into clean layers (Controller, DTO, Service, Entity, Repository, Exception, Config).
3. To maintain version control using Git and host the code publicly on GitHub.
4. To write a JUnit 5 integration test suite verifying core application flows.

---

## 3. SCOPE OF THE PROJECT

### 3.1 Scope Included in the Present Version
- Shortening long URLs into random 6-character keys or user-defined custom aliases.
- Sub-millisecond HTTP 302 redirection backed by Redis caching with SQL fallback.
- Input URL validation, scheme checking, and automatic protocol prepending (`https://`).
- Link expiration policy support (1 day, 7 days, 30 days, or never).
- Real-time click count tracking and metadata analytics.
- Dynamic PNG QR code generation.
- Modern glassmorphism UI with one-click copying and toast notifications.
- In-memory H2 DB default + Docker Compose setup (PostgreSQL 16 + Redis 7).

### 3.2 Scope Deliberately Excluded
- User login / JWT session authentication (planned for future release).
- Custom domain mapping (`mybrand.link`).
- Geographical IP location tracking for clicks.

---

## 4. TECHNOLOGIES AND TOOLS USED

| Technology / Tool | Role in the Project |
|---|---|
| **Java 17 (Standard Edition)** | Core programming language for business logic, services, and entities. |
| **Spring Boot 3.3.4** | Framework providing Spring Web MVC, Dependency Injection, and Spring Data JPA. |
| **Redis** | In-memory key-value cache storing `shortCode -> originalUrl` for sub-millisecond lookups. |
| **Jakarta Validation** | Enforces input constraints (`@NotBlank`, `@Size`, `@Pattern`) on DTOs. |
| **H2 Database / PostgreSQL** | Relational databases for persistent entity storage. |
| **ZXing (Zebra Crossing) 3.5.3** | QR Code generation library producing PNG image streams. |
| **Maven** | Dependency resolution and build automation tool. |
| **Docker & Docker Compose** | Orchestrates PostgreSQL 16 and Redis 7 containers. |
| **HTML5, CSS3, Vanilla JS** | Web UI with dark-mode styling, glassmorphism cards, and Fetch API. |
| **Git & GitHub** | Source code version control and public repository hosting. |

---

## 5. SYSTEM REQUIREMENTS

### 5.1 Hardware Requirements
- **Processor:** Dual-Core 1.8 GHz or higher
- **RAM:** 4 GB minimum (8 GB recommended for running Docker containers)
- **Disk Space:** 500 MB free space (JDK, Maven cache, project files)

### 5.2 Software Requirements
- **Operating System:** Windows 10/11, Linux (Fedora/Ubuntu), or macOS
- **JDK:** OpenJDK 17 or Java 25
- **Maven:** Version 3.8+
- **Browser:** Google Chrome, Mozilla Firefox, or Microsoft Edge

---

## 6. SYSTEM DESIGN

### 6.1 Overall Architecture

SwiftLink follows a **Layered Microservice Architecture**:

```
+--------------------------------------------------------------------------+
|                            USER (Browser / API Client)                   |
+--------------------------------------------------------------------------+
                                    |
                                    v (HTTP Requests)
+--------------------------------------------------------------------------+
| PRESENTATION LAYER (Url_Controller.java, index.html)                    |
| - REST endpoints, Request Body Validation (@Valid), JSON Serialization  |
+--------------------------------------------------------------------------+
                                    |
                                    v
+--------------------------------------------------------------------------+
| SERVICE LAYER (UrlService.java)                                          |
| - URL Normalization, Custom Alias Logic, Expiration Check, QR Generator  |
+--------------------------------------------------------------------------+
                   |                                       |
                   v (Check Cache First)                   v (Cache Miss / Save)
+------------------------------------+   +---------------------------------+
| REDIS CACHE LAYER (RedisConfig)    |   | PERSISTENCE LAYER               |
| - StringRedisTemplate              |   | - Spring Data JPA Repository    |
| - Sub-millisecond URL lookups      |   | - UrlMapping Entity (H2 / Postgres)
+------------------------------------+   +---------------------------------+
```

### 6.2 Module Structure and Package Organisation

```
com.example.Url_Shortner/
├── UrlShortnerApplication.java         # Main Entry Point
├── config/
│   └── RedisConfig.java                # RedisTemplate & CacheManager Beans
├── controller/
│   └── Url_Controller.java             # REST Endpoints (/api/shorten, /api/{code}, etc.)
├── dto/
│   ├── UrlRequest.java                 # Validated Request DTO
│   ├── UrlResponse.java                # Short URL Response DTO
│   ├── UrlAnalyticsResponse.java       # Analytics Metrics DTO
│   └── ErrorResponse.java              # Standardized Error JSON Structure
├── entity/
│   └── UrlMapping.java                 # JPA Entity mapped to 'url_mapping' table
├── exception/
│   ├── GlobalExceptionHandler.java     # @RestControllerAdvice handling exceptions
│   ├── UrlNotFoundException.java       # Custom 404 Exception
│   ├── UrlExpiredException.java        # Custom 410 Gone Exception
│   ├── InvalidUrlException.java        # Custom 400 Bad Request Exception
│   └── AliasAlreadyExistsException.java # Custom 409 Conflict Exception
├── repository/
│   └── UrlMappingRepository.java       # JpaRepository interface with indexed lookups
└── service/
    └── UrlService.java                 # Core logic, Redis integration, QR engine
```

---

## 7. PROJECT MODULES

### 7.1 URL Shortening & Normalization Module
Accepts long URLs, verifies scheme validity, prepends `https://` if protocol is missing, validates against dangerous schemes, checks for custom alias availability, and generates a unique 6-character Base62/random string code.

### 7.2 Redis Caching & Redirection Module
Performs high-speed lookups in Redis for `GET /api/{shortCode}`. Serves HTTP 302 redirects in under 1 millisecond. If Redis is down, degrades gracefully to SQL querying without disrupting service.

### 7.3 Analytics & QR Code Generation Module
Calculates total visit counts, checks expiration status, and generates PNG QR codes using ZXing `QRCodeWriter` for mobile scanning.

### 7.4 Web Interface & Dashboard Module
Static HTML5/JS single-page dashboard (`index.html`) featuring a glassmorphism theme, advanced options accordion, one-click copy toast, QR modal, analytics drawer, and local storage history.

### 7.5 Validation & Exception Handling Module
Uses `jakarta.validation` annotations and `@RestControllerAdvice` to trap validation failures, invalid syntax, expired links, and duplicate aliases, returning clean JSON error responses instead of stack traces.

---

## 8. OBJECT-ORIENTED PROGRAMMING CONCEPTS USED

1. **Classes and Objects:** Real-world entities such as `UrlMapping`, `UrlRequest`, `UrlResponse`, and `UrlAnalyticsResponse` are encapsulated as Java classes and instantiated as objects.
2. **Encapsulation:** Private entity fields with public getters/setters, protecting state integrity and preventing direct mutation.
3. **Abstraction:** Service interfaces and repositories expose business operations (`createShortUrl`, `getOriginalUrl`) while hiding caching and database query implementation details.
4. **Inheritance and Polymorphism:** Extends standard `RuntimeException` in custom exception classes (`UrlNotFoundException`, `UrlExpiredException`) and leverages polymorphic exception handling in `GlobalExceptionHandler`.
5. **Modularity:** Package-based segregation (`controller`, `service`, `repository`, `entity`, `dto`, `config`, `exception`).
6. **Reusability:** Shared DTOs, utility validation functions, and reusable RedisTemplate beans.
7. **Exception Handling:** Robust try-catch blocks, graceful Redis fallback, and global exception mapping.

---

## 9. WORKING OF THE APPLICATION

### 9.1 Compilation and Execution
```bash
# Compile and build using Maven
mvn clean compile

# Execute tests
mvn test

# Run application
mvn spring-boot:run
```

### 9.2 Start-up Sequence
1. Spring Boot initializes `ApplicationContext`.
2. JPA creates/updates the `url_mapping` database table and index on `shortCode`.
3. Redis configuration establishes `RedisTemplate` connection pool.
4. Web server starts on port `8080` serving static assets and REST endpoints.

---

## 10. SAMPLE INPUT AND OUTPUT

### 10.1 Shorten URL Request & Response
**POST** `/api/shorten`
```json
// Request
{
  "originalUrl": "https://example.com/long/path"
}

// Response (HTTP 201 Created)
{
  "shortUrl": "http://localhost:8080/api/hVI4kw",
  "originalUrl": "https://example.com/long/path",
  "shortCode": "hVI4kw",
  "createdAt": "2026-09-18T19:30:00",
  "expiresAt": null,
  "qrCodeUrl": "http://localhost:8080/api/qr/hVI4kw"
}
```

### 10.2 Custom Alias & Expiration Request
**POST** `/api/shorten`
```json
// Request
{
  "originalUrl": "https://vit.ac.in",
  "customAlias": "vit-custom-code",
  "expirationDays": 7
}

// Response (HTTP 201 Created)
{
  "shortUrl": "http://localhost:8080/api/vit-custom-code",
  "originalUrl": "https://vit.ac.in",
  "shortCode": "vit-custom-code",
  "createdAt": "2026-09-18T19:30:00",
  "expiresAt": "2026-09-25T19:30:00",
  "qrCodeUrl": "http://localhost:8080/api/qr/vit-custom-code"
}
```

### 10.3 Duplicate Custom Alias Rejection
```json
// Response (HTTP 409 Conflict)
{
  "timestamp": "2026-09-18T19:31:00",
  "status": 409,
  "error": "Alias Conflict",
  "message": "Custom alias 'vit-custom-code' is already in use."
}
```

### 10.4 Invalid / Empty URL Validation Error
```json
// Response (HTTP 400 Bad Request)
{
  "timestamp": "2026-09-18T19:32:00",
  "status": 400,
  "error": "Validation Failed",
  "message": "One or more request parameters failed validation",
  "fieldErrors": {
    "originalUrl": "Original URL cannot be null, empty, or blank"
  }
}
```

---

## 11. TESTING

Automated integration test execution results from `mvn test`:

| ID | Test Name | Input / Operation | Expected Result | Status |
|---|---|---|---|---|
| T-01 | `contextLoads` | Application startup | ApplicationContext loads cleanly | **PASS** |
| T-02 | `testCreateShortUrl` | Valid URL submission | Generates 6-char code & normalizes protocol | **PASS** |
| T-03 | `testCustomAliasFlow` | Custom alias & duplicate submission | Creates custom shortCode & rejects duplicate (409) | **PASS** |
| T-04 | `testInvalidUrlValidation` | Empty/blank URL submission | Rejects payload with 400 Bad Request | **PASS** |
| T-05 | `testGetOriginalUrlAndClickCount` | Redirect lookup (2x) | Resolves original URL & click count equals 2 | **PASS** |
| T-06 | `testQrCodeGeneration` | QR endpoint request | Returns `image/png` byte array stream | **PASS** |

---

## 12. ADVANTAGES

- **Sub-Millisecond Performance:** Redis caching eliminates database latency during redirect spikes.
- **Zero-Crash Defensive Validation:** Pre-validates payloads, preventing `NullPointerException` or malformed data corruption.
- **Zero Installation Overhead:** Runs instantly with H2 in-memory database out of the box.
- **Feature Rich:** Custom aliases, QR codes, link expiration, and analytics built-in.
- **Modern UI:** Glassmorphism dashboard with dark-mode theme and clipboard notifications.

---

## 13. LIMITATIONS

- **Authentication:** Current version operates without user login sessions (single-tenant).
- **Custom Domains:** Short URLs use host server domain name.

---

## 14. FUTURE ENHANCEMENTS

1. **User Authentication (JWT):** Enable user login, registration, and user-specific link management dashboards.
2. **Custom Branded Domains:** Allow users to connect custom short domains (e.g., `shubham.link`).
3. **Geo-Location & Device Analytics:** Track visitor country, browser type, and operating system.
4. **Distributed Rate Limiting:** Implement Redis token bucket algorithm to protect against DDoS scraping.

---

## 15. CONCLUSION

The **URL Shortener & Analytics Service (SwiftLink)** successfully fulfills the objectives established for this project. By integrating **Spring Boot**, **Redis Caching**, **Jakarta Validation**, and **ZXing QR generation**, it provides a robust, scalable web application capable of serving high-traffic redirects at sub-millisecond speeds. The project consolidated core Java principles, layered architecture, data persistence, and modern web design into a complete end-to-end software solution.

---

## 16. REFERENCES

1. Oracle Corporation, *Java Platform, Standard Edition Documentation*, Available: https://docs.oracle.com/en/java/
2. Spring Boot Reference Documentation, *Spring Framework*, Available: https://spring.io/projects/spring-boot
3. Redis Documentation, *Redis In-Memory Data Store*, Available: https://redis.io/docs/
4. H. Schildt, *Java: The Complete Reference*, McGraw-Hill Education.

---

## 17. GITHUB REPOSITORY

The complete source code of the project, including tests, configuration files, and documentation, is maintained in a public GitHub repository.

| Attribute | Details |
|---|---|
| **Repository URL** | https://github.com/Shubhamcoder0806/Url_Shortner |
| **Owner** | Shubham Mishra (Shubhamcoder0806) |
| **Project Name** | URL Shortener & Analytics Service (SwiftLink) |
| **Language / Framework** | Java 17 / Spring Boot 3.3.4 |
| **Access** | Public |

```bash
git clone https://github.com/Shubhamcoder0806/Url_Shortner.git
```

---

<br>

<div align="center">
  <h3>END OF REPORT</h3>
  <p><b>Shubham Mishra</b> | <b>24BCY10064</b> | B.Tech Computer Science and Engineering<br>
  VIT Bhopal University | Programming in Java</p>
</div>
