# URL Shortener

A simple URL shortener built with Spring Boot, MariaDB/MySQL, and a plain HTML/JS frontend. Users can submit a long URL and get back a short link that redirects to the original address.

## Tech Stack

- **Backend:** Java, Spring Boot
- **Database:** MariaDB (MySQL-compatible)
- **ORM:** Spring Data JPA / Hibernate
- **Frontend:** HTML, CSS, vanilla JavaScript (served from Spring Boot's `static` folder)

## Features

- Shorten any long URL into a random 6-character code
- Redirect from the short URL to the original URL
- Tracks click count per short URL
- Clean error handling for invalid/unknown short codes

## Project Structure

```
src/
├── main/
│   ├── java/com/example/Url_Shortner/
│   │   ├── UrlShortnerApplication.java   # Main entry point
│   │   ├── controller/                   # REST API endpoints
│   │   ├── service/                      # Business logic (short code generation, lookups)
│   │   ├── repository/                   # Database access (Spring Data JPA)
│   │   ├── entity/                       # Database table mapping (UrlMapping)
│   │   ├── dto/                          # Request/response objects
│   │   └── exception/                    # Custom exceptions + global error handler
│   └── resources/
│       ├── application.properties        # Database and server config
│       └── static/
│           └── index.html                # Simple frontend
```

## Prerequisites

- Java 17 or higher
- Maven
- MariaDB or MySQL installed and running

## Setup

**1. Clone the repository**
```bash
git clone https://github.com/Shubhamcoder0806/Url_Shortner.git
cd Url_Shortner
```

**2. Create the database**

Log into MySQL/MariaDB:
```bash
mysql -u root -p
```
Then run:
```sql
CREATE DATABASE url_shortener_db;
```

**3. Configure database credentials**

Open `src/main/resources/application.properties` and update:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/url_shortener_db
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD
```

**4. Run the application**

Using Maven:
```bash
mvn spring-boot:run
```
Or run `UrlShortnerApplication.java` directly from your IDE.

The app will start on `http://localhost:8080`. Hibernate will auto-create the required database table on first run.

## Usage

**Via the frontend**

Open `http://localhost:8080` in your browser, paste a long URL, and click Shorten.

**Via the API directly**

Create a short URL:
```bash
curl -X POST http://localhost:8080/api/shorten \
  -H "Content-Type: application/json" \
  -d '{"originalUrl": "https://example.com"}'
```

Response:
```json
{
  "shortUrl": "http://localhost:8080/api/abc123",
  "originalUrl": "https://example.com"
}
```

Visit the short URL to be redirected to the original:
```bash
curl -v http://localhost:8080/api/abc123
```

## API Endpoints

| Method | Endpoint            | Description                          |
|--------|----------------------|---------------------------------------|
| POST   | `/api/shorten`       | Create a short URL from a long URL    |
| GET    | `/api/{shortCode}`   | Redirect to the original URL          |

## Notes

- `spring.jpa.hibernate.ddl-auto=update` is used for development — the database schema updates automatically to match the code. Switch to `validate` (or use a migration tool like Flyway) before production use.
- Never commit real database credentials to a public repository — use environment variables or a `.env` file excluded via `.gitignore` for production deployments.

## Roadmap

- User authentication (JWT-based)
- Per-user link management dashboard
- Custom short codes
- Redis caching for high-traffic redirects
- React frontend
