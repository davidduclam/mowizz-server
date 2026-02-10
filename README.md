# MoWizz Server

MoWizz Server is a Spring Boot backend for movie and TV discovery using the TMDB API, with PostgreSQL persistence for user data.

## Overview

This service provides:
- TMDB-backed movie discovery endpoints
- TMDB-backed TV show discovery endpoints
- Multi-type search (movies, TV shows) via TMDB
- Basic user management endpoints

## System Architecture

```mermaid
flowchart LR

    subgraph CLIENT["Client Side"]
        WEB["User"]
    end

    subgraph APP["MoWizz Server"]
        direction TB

        subgraph API["API Layer"]
            MC["MovieController"]
            TSC["TvShowController"]
            SC["SearchController"]
            UC["UserController"]
            UMC["UserMediaController"]
        end

        subgraph BIZ["Service Layer"]
            MS["MovieService"]
            TSS["TvShowService"]
            SS["SearchService"]
            US["UserService"]
            UMS["UserMediaService"]
        end

        subgraph DATA["Persistence Layer"]
            MR["MovieRepository"]
            TSR["TvShowRepository"]
            UR["UserRepository"]
            UMR["UserMediaRepository"]
        end

        TC["TmdbClient"]
    end

    subgraph EXT_API["External API Systems"]
        TMDB["TMDB API"]
    end

    subgraph EXT_DATA["External Database"]
        DB["Supabase"]
    end

    WEB --> API

    MC --> MS
    TSC --> TSS
    SC --> SS
    UC --> US
    UMC --> UMS

    UMS --> UMR

    MS --> TC
    TSS --> TC
    SS --> TC
    TC --> TMDB

    MS --> MR
    TSS --> TSR
    US --> UR

    MR --> DB
    TSR --> DB
    UR --> DB
    UMR --> DB
```

## Tech Stack

- Java 21
- Spring Boot 4.0.1
- Spring Web MVC
- Spring Data JPA
- Flyway
- PostgreSQL
- Maven Wrapper (`./mvnw`)

## Prerequisites

- Java 21+
- PostgreSQL instance
- TMDB API access token

## Configuration

Create a `.env` file in the project root (or export environment variables directly):

```bash
export DB_URL=your_db_url
export DB_USERNAME=your_username
export DB_PASSWORD=your_password
export TMDB_ACCESS_TOKEN=your_tmdb_access_token
```

Reference template: `.env.example`

## Running Locally

1. Install dependencies and run the application:

```bash
./mvnw spring-boot:run
```

2. API will be available at:

```text
http://localhost:8080
```

Flyway migrations run at startup, and Hibernate is configured with `ddl-auto=validate`.

## Build and Test

```bash
./mvnw clean verify
```

## API Endpoints

### Movies (`/movies`)
- `GET /movies/search?query=...`
- `GET /movies/{tmdbId}`
- `GET /movies/popular`
- `GET /movies/top-rated`
- `GET /movies/upcoming`

### TV Shows (`/shows`)
- `GET /shows/{tmdbId}`
- `GET /shows/popular`
- `GET /shows/top-rated`

### Search (`/search`)
- `GET /search/multi?query=...`

### Users (`/users`)
- `POST /users`
- `GET /users`
- `GET /users/{id}`
- `DELETE /users/{id}`

## OpenAPI Specification

- `openapi.yaml`

## License

This project is licensed under the MIT License. See `LICENSE` for details.
