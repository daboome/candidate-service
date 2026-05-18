# Candidate Registration Service

Micronaut microservice for CFA-style candidate registration and eligibility verification.

## Stack

- Java 21, Micronaut 4 (Netty), virtual threads
- Gradle multi-module: `api`, `application`, `domain`, `infrastructure`
- PostgreSQL, JPA/Hibernate, Liquibase
- Spotless, JSON logging (Logback + MDC correlation ID)
- JUnit 5, Mockito, Testcontainers

## Architecture

Hexagonal layout with dependency direction **api → application → domain ← infrastructure**.

```text
api/              REST controllers, DTOs, RFC 7807 errors
application/      Use cases, Micronaut bootstrap
domain/           Model, eligibility rules, ports
infrastructure/   JPA adapters, Liquibase, async jobs
```

See [DECISIONS.md](DECISIONS.md) for architectural choices and [docs/adr/](docs/adr/) for focused ADRs.

## Prerequisites

- JDK 21+
- Docker (for PostgreSQL via Compose)

## Run locally

```bash
# Start PostgreSQL
docker compose up -d postgres

# Run the service
./gradlew :application:run
```

API base URL: `http://localhost:8080/api/v1/candidates`

VS Code [REST Client](https://marketplace.visualstudio.com/items?itemName=humao.rest-client) requests: [`http/candidate-api.http`](http/candidate-api.http)

OpenAPI spec is generated at build time under `application/build/classes/java/main/META-INF/swagger/`.

## Build & test

```bash
./gradlew spotlessCheck build
./gradlew :domain:test jacocoTestReport   # domain coverage >= 80%
./gradlew :application:test               # includes Testcontainers IT
```

## Docker Compose (app + DB)

```bash
./gradlew :application:shadowJar
docker compose up --build
```

## API overview

| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/v1/candidates` | Register candidate |
| GET | `/api/v1/candidates/{id}` | Get candidate + eligibility status |
| PUT | `/api/v1/candidates/{id}/eligibility` | Trigger async verification (202) |
| GET | `/api/v1/candidates?status=&program=` | Search with pagination |
| DELETE | `/api/v1/candidates/{id}` | Soft delete |

Pass `X-Correlation-Id` for request tracing; echoed in response and JSON logs.

## Trade-offs

- **Soft delete** preserves audit history; email uniqueness applies only to active rows.
- **Async eligibility** returns immediately with `VERIFYING`; clients poll GET for final status.
- **Child entity IDs** regenerated on update for simplicity (acceptable for this exercise).
