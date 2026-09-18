# SwiftLink — High-Performance URL Shortener & Analytics Service

SwiftLink is a high-throughput, production-ready RESTful web application built with **Java 17**, **Spring Boot**, and **Redis Caching**. It converts long, unwieldy URLs into compact, shareable links, serving redirects at sub-millisecond latency using Redis caching.

The application includes robust input validation (preventing empty, null, or malformed URL crashes), custom link aliases, link expiration, dynamic QR code generation, click analytics, multi-database support (H2, PostgreSQL, MySQL), and a modern glassmorphism web interface.

---

## ✨ Features

- **⚡ Sub-Millisecond Redis Caching**: Redirect lookups fetch directly from Redis cache first, bypassing database roundtrips for maximum throughput.
- **🛡️ Defensive Input Validation & Normalization**: Jakarta Validation (`@NotBlank`, `@Size`, `@Pattern`) prevents empty/null or malformed inputs. Automatically normalizes missing URL protocols (`https://`) and blocks malicious URI schemes.
- **🎨 Custom Link Aliases**: Users can define personalized short codes (e.g. `http://localhost:8080/api/my-custom-code`) with conflict checking.
- **⏳ Link Expiration**: Optional expiration policy (1 day, 7 days, 30 days, or never). Automatically returns HTTP 410 (Gone) for expired links.
- **📱 Dynamic QR Code Generator**: Generates mobile-scannable PNG QR codes via `/api/qr/{shortCode}` using ZXing.
- **📊 Real-Time Click Analytics**: Tracks total visits, creation dates, expiration timestamps, and target destination details.
- **🗄️ Multi-Database & Zero-Config Execution**: H2 in-memory database configured by default for instant local testing out of the box, with full support for MySQL, MariaDB, and PostgreSQL.
- **🐳 Docker Compose Support**: Spin up PostgreSQL and Redis infrastructure with a single `docker compose up -d` command.
- **💎 Glassmorphism Dark-Mode Dashboard**: Sleek responsive web UI with one-click copying, toast notifications, QR preview modal, analytics drawer, and local storage link history.

---

## 🛠️ Technology Stack

| Component | Technology | Description |
|---|---|---|
| **Language** | Java 17+ | Core programming language |
| **Framework** | Spring Boot 3.3.4 | REST API, Spring MVC, Spring Data JPA |
| **Caching Engine** | Redis | High-speed caching for sub-millisecond redirects |
| **Databases** | H2 (Dev) / PostgreSQL / MySQL | Data persistence with indexed `short_code` lookup |
| **Validation** | Jakarta Validation | Strict request payload constraint checking |
| **QR Engine** | ZXing 3.5.3 | PNG QR Code generation |
| **Build Tool** | Maven | Dependency management & project build |
| **Containers** | Docker & Docker Compose | Containerized PostgreSQL and Redis services |
| **Frontend** | HTML5, CSS3, Vanilla JS | Modern glassmorphism dark-mode interface |

---

## 📂 Project Structure

```
Url_Shortner/
├── docker-compose.yml                  # Infrastructure setup (PostgreSQL + Redis)
├── pom.xml                             # Maven build & dependencies
├── README.md                           # Documentation
└── src/
    ├── main/
    │   ├── java/com/example/Url_Shortner/
    │   │   ├── UrlShortnerApplication.java   # Spring Boot entry point
    │   │   ├── config/                       # Redis & CacheManager configuration
    │   │   ├── controller/                   # REST API Endpoints
    │   │   ├── dto/                          # Request, Response, Analytics & Error DTOs
    │   │   ├── entity/                       # JPA Entity (UrlMapping)
    │   │   ├── exception/                    # Custom Exceptions & Global Exception Handler
    │   │   ├── repository/                   # Spring Data JPA Repository
    │   │   └── service/                      # Core Business Logic & Caching Engine
    │   └── resources/
    │       ├── application.properties        # App, Database, and Redis properties
    │       └── static/
    │           └── index.html                # Modern Glassmorphism Dashboard
    └── test/
        └── java/com/example/Url_Shortner/    # Integration & Unit Test Suite
```

---

## 🚀 Quick Start Guide

### Prerequisites
- **Java 17** or higher installed (`java -version`)
- **Maven** 3.8+ installed (`mvn -version`)
- *(Optional)* **Docker & Docker Compose** for Redis & PostgreSQL infrastructure

---

### Method 1: Instant Local Run (In-Memory H2 DB)

By default, the application uses an in-memory **H2 database**. It runs out of the box without requiring manual database installations.

1. **Clone the repository**
   ```bash
   git clone https://github.com/Shubhamcoder0806/Url_Shortner.git
   cd Url_Shortner
   ```

2. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

3. **Access the Dashboard**
   Open `http://localhost:8080` in your web browser.
   - H2 Database Web Console: `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:url_shortener_db`, Username: `sa`, Password: leave blank)

---

### Method 2: Production Setup with Docker Compose (PostgreSQL + Redis)

1. **Start PostgreSQL and Redis containers**
   ```bash
   docker compose up -d
   ```

2. **Configure Database Credentials**
   In `src/main/resources/application.properties`, uncomment the PostgreSQL configuration:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/url_shortener_db
   spring.datasource.username=postgres
   spring.datasource.password=postgrespassword
   spring.datasource.driver-class-name=org.postgresql.Driver
   ```

3. **Run the Spring Boot application**
   ```bash
   mvn spring-boot:run
   ```

---

## 📡 REST API Reference

### 1. Shorten a URL
- **Endpoint**: `POST /api/shorten`
- **Headers**: `Content-Type: application/json`

**Sample Request (Basic)**:
```json
{
  "originalUrl": "https://example.com/very/long/path/to/page"
}
```

**Sample Request (Custom Alias & Expiration)**:
```json
{
  "originalUrl": "https://vit.ac.in",
  "customAlias": "vit-portal",
  "expirationDays": 7
}
```

**Sample Response (HTTP 201 Created)**:
```json
{
  "shortUrl": "http://localhost:8080/api/vit-portal",
  "originalUrl": "https://vit.ac.in",
  "shortCode": "vit-portal",
  "createdAt": "2026-09-18T19:30:00",
  "expiresAt": "2026-09-25T19:30:00",
  "qrCodeUrl": "http://localhost:8080/api/qr/vit-portal"
}
```

---

### 2. Redirect Short Link
- **Endpoint**: `GET /api/{shortCode}`
- **Response**: `HTTP 302 Found` with `Location` header pointing to the original URL.
- **curl example**:
  ```bash
  curl -v http://localhost:8080/api/vit-portal
  ```

---

### 3. Click Analytics & Metrics
- **Endpoint**: `GET /api/analytics/{shortCode}`
- **Response (HTTP 200 OK)**:
```json
{
  "shortCode": "vit-portal",
  "originalUrl": "https://vit.ac.in",
  "shortUrl": "http://localhost:8080/api/vit-portal",
  "clickCount": 42,
  "createdAt": "2026-09-18T19:30:00",
  "expiresAt": "2026-09-25T19:30:00",
  "expired": false
}
```

---

### 4. Generate QR Code Image
- **Endpoint**: `GET /api/qr/{shortCode}`
- **Response**: `HTTP 200 OK` (`image/png` byte stream)
- **HTML / Markdown usage**:
  ```html
  <img src="http://localhost:8080/api/qr/vit-portal" alt="QR Code">
  ```

---

## 🧪 Testing

Run the automated integration test suite:
```bash
mvn test
```

The test suite validates:
- URL normalization & scheme security
- Null / empty payload validation error responses
- Custom alias creation & duplicate conflict handling
- HTTP 302 redirection logic and click metric tracking
- Dynamic QR code generation stream

---

## 🛡️ Exception & Error Response Format

All validation or operational errors return a structured JSON response instead of server crashes:

```json
{
  "timestamp": "2026-09-18T19:35:00",
  "status": 400,
  "error": "Validation Failed",
  "message": "One or more request parameters failed validation",
  "fieldErrors": {
    "originalUrl": "Original URL cannot be null, empty, or blank"
  }
}
```

---

## 📜 License

Distributed under the MIT License. Free for college evaluation, commercial use, and personal projects.
