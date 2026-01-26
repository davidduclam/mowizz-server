# MoWizz Server

Backend service for MoWizz, providing movie discovery via TMDB and basic user management.
Built with Spring Boot, JPA, Flyway, and PostgreSQL.

## Features
- Search and fetch movies from TMDB (popular, top-rated, upcoming, and by ID)
- Basic user CRUD endpoints
- Database migrations with Flyway
- OpenAPI spec included for current endpoints

## Tech Stack
- Java 21
- Spring Boot 4
- Spring Web MVC, Spring Data JPA, Validation
- PostgreSQL + Flyway
- Maven

## Requirements
- Java 21+
- PostgreSQL
- TMDB API access token

## Configuration
Set the following environment variables (see `src/main/resources/application.properties`):

```bash
export DB_URL=your_db_url
export DB_USERNAME=your_username
export DB_PASSWORD=your_password
export TMDB_ACCESS_TOKEN=your_tmdb_access_token
```

## Run Locally
```bash
./mvnw spring-boot:run
```

The server starts on `http://localhost:8080`.

## API Reference
- OpenAPI spec: `openapi.yaml`
- Base URL: `http://localhost:8080`

### Movie Endpoints
- `GET /movie/search?query=...`
- `GET /movie/{tmdbId}`
- `GET /movie/popular`
- `GET /movie/top-rated`
- `GET /movie/upcoming`

### User Endpoints
- `POST /users`
- `GET /users`
- `GET /users/{id}`
- `DELETE /users/{id}`

## Database Notes
- Flyway runs on startup; set `DB_*` vars before running.
- Hibernate is configured with `ddl-auto=validate`.
