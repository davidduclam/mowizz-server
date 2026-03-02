# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Build and run all tests
./mvnw clean verify

# Run the application locally (requires .env with DB and TMDB credentials)
./mvnw spring-boot:run

# Run with Docker (includes PostgreSQL)
export TMDB_ACCESS_TOKEN=your_token
docker compose up --build
```

Tests use an H2 in-memory database (PostgreSQL-compatibility mode) with Flyway disabled — no external dependencies needed to run them.

## Environment Variables

Create a `.env` file in the project root (see `.env.example`):

```
DB_URL=jdbc:postgresql://...
DB_USERNAME=...
DB_PASSWORD=...
TMDB_ACCESS_TOKEN=...
```

## Architecture

Standard Spring Boot layered architecture with 5 domains: **Movie**, **TvShow**, **Search**, **User**, **UserMedia**.

```
Controller → Service → Repository (JPA / PostgreSQL)
                  ↘ TmdbClient → TMDB API
```

`UserMediaService` acts as an orchestrator and calls into `MovieService` and `TvShowService` (as well as their repositories directly). This service-to-service pattern is intentional.

- **`client/tmdb/TmdbClient`** — wraps Spring's `RestClient` with Bearer token auth. Uses a supplier-based execute pattern for consistent logging and error wrapping. All TMDB responses deserialize into DTOs under `client/tmdb/dto/`.
- **`model/`** — JPA entities. `MediaType` enum (values: `TV`, `MOVIE`) unifies media tracking in `UserMedia`.
- **`dto/`** — Own API types, separate from TMDB wire format types:
  - `MovieResponseDTO`, `TvShowResponseDTO` — full detail response payloads (camelCase fields)
  - `MovieSearchResultDTO`, `TvShowSearchResultDTO` — lightweight search result payloads, both implement the `SearchResultDTO` sealed interface
  - `AddUserMediaRequestDTO` — request body for the user media endpoint
- **`error/GlobalExceptionHandler`** — `@RestControllerAdvice` mapping custom exceptions to HTTP status codes (e.g. `TmdbClientException` → 503, `UserNotFoundException` → 404, `MediaAlreadyExistsException` → 409).

**Database:** PostgreSQL (Supabase) in production; Flyway manages migrations with `baseline-on-migrate=true` and Hibernate set to `ddl-auto=validate`. The `db/migration/` directory is currently empty — schema is created by `ddl-auto=update` in Docker Compose.

**Key transaction boundary:** `UserMediaService.addMediaToUser` is `@Transactional`, as it coordinates lookups/saves across multiple repositories.

## Testing

The `test` Spring profile (activated automatically during tests) swaps in H2 with `MODE=PostgreSQL` and `ddl-auto=create-drop`. There is currently only a context-load smoke test — new tests should use `@SpringBootTest` with the `test` profile.
