# Architectural Decisions

This document summarizes the main design choices for the **Candidate Registration Service**. Each section states the decision, the rationale, and the trade-offs. Detailed write-ups live under [`docs/adr/`](docs/adr/).

---

## 1. Hexagonal architecture with four Gradle modules

**Decision:** Split the codebase into `domain`, `application`, `infrastructure`, and `api`, with dependencies pointing inward toward the domain.

```text
api  ──►  application  ──►  domain  ◄──  infrastructure
              │                              │
              └──────── runtimeOnly(api) ────┘
```

| Module | Responsibility |
|--------|----------------|
| `domain` | Entities, eligibility rules, repository **ports**, domain exceptions |
| `application` | Use cases, Micronaut bootstrap, `application.yml` |
| `infrastructure` | JPA adapters, Liquibase, async eligibility worker |
| `api` | REST controllers, DTOs, RFC 7807 handler, correlation filter |

**Why:** Keeps business rules free of framework and database code, makes the domain unit-testable without containers, and matches the assessment’s Clean Architecture requirement.

**Trade-off:** More modules and wiring than a single-package app. `api` is a `runtimeOnly` dependency of `application` so Gradle stays acyclic while Micronaut scans `com.cfa.candidate` at runtime.

**See also:** [ADR 001](docs/adr/001-hexagonal-modules.md)

---

## 2. Micronaut (not Spring Boot) on Java 21 with virtual threads

**Decision:** Use **Micronaut 4** on **Netty**, **classic (blocking) APIs**, and **virtual threads** for async eligibility work—not a reactive stack.

**Why:**

- Explicit stack requirement for the exercise.
- Compile-time DI and lower memory footprint than typical Spring Boot setups.
- Virtual threads (`Executors.newVirtualThreadPerTaskExecutor`) give cheap concurrency for fire-and-forget eligibility without Project Reactor or RxJava.

**Trade-off:** Team familiarity may favor Spring; reactive Micronaut was avoided to keep JPA and use cases straightforward.

---

## 3. Pure domain layer for eligibility rules

**Decision:** Implement all Level I–III rules in `EligibilityEvaluator` in `domain`, with no Micronaut or JPA imports.

| Program | Rule |
|---------|------|
| Level I | Bachelor’s (or higher) degree **or** ≥ 4 years professional experience |
| Level II | Passed Level I within the last 5 years |
| Level III | Passed Level II within the last 5 years |

**Why:** Rules are the core business value; isolating them enables fast unit tests and clear auditability (“what decision, why” in the audit trail).

**Trade-off:** Exam passes and experience are modeled on the aggregate; a richer certification bounded context might use separate aggregates and domain events.

---

## 4. Asynchronous eligibility verification

**Decision:** `PUT /api/v1/candidates/{id}/eligibility` returns **202 Accepted** with status `VERIFYING`, then runs evaluation on a background virtual-thread task. Clients poll `GET /{id}` for `ELIGIBLE` / `INELIGIBLE`.

**Flow:**

```text
Client ──PUT /eligibility──► Use case sets VERIFYING ──► Scheduler ──► Job
                                                                    │
                                    ◄── GET /{id} (poll) ───────────┤
                                    Audit row + status update ◄─────┘
```

**Why:** Separates HTTP latency from rule evaluation and audit persistence; satisfies the “asynchronous business rule evaluation” requirement without messaging infrastructure.

**Trade-off:** Eventual consistency; duplicate triggers are rejected while `VERIFYING`. Production might use a queue, idempotency keys, and retries instead of in-process execution.

**See also:** [ADR 002](docs/adr/002-async-eligibility.md)

---

## 5. Eligibility audit trail

**Decision:** Every completed verification appends an `eligibility_audits` row: `verified_by`, `verified_at`, `decision`, `reason`.

**Why:** Supports compliance and debugging (“who, when, what, why”).

**Trade-off:** Audits are not exposed on the public API in this version; a future `GET /{id}/audits` would be a thin read use case.

---

## 6. PostgreSQL, JPA, and Liquibase

**Decision:**

- **PostgreSQL** as the system of record.
- **Micronaut Data JPA / Hibernate** for persistence; schema owned by **Liquibase** (`001-initial-schema`, `002-seed-data`).
- **No** `hbm2ddl.auto`; schema changes only via changelogs.

**Why:** Aligns with mandated stack; Liquibase gives repeatable migrations and seed data for local dev and integration tests.

**Trade-off:** JPA entity ↔ domain mapping lives in `infrastructure` (manual `CandidateMapper`). MapStruct could reduce boilerplate at the cost of another dependency.

---

## 7. Soft delete and active-only email uniqueness

**Decision:**

- Soft delete via `deleted_at`; all reads and searches exclude deleted rows.
- Partial unique index: `email` unique **where** `deleted_at IS NULL`.
- `DuplicateEmailException` (409) on constraint violation.

**Why:** Preserves history and audit referential integrity while enforcing “one active registration per email.”

**Trade-off:** Re-registration with the same email is allowed after soft delete; product owners must confirm that is intended.

**See also:** [ADR 003](docs/adr/003-soft-delete-and-email-uniqueness.md)

---

## 8. API design: DTOs, validation, RFC 7807

**Decision:**

- REST resources under `/api/v1/candidates`.
- **Jakarta Validation** on request DTOs at the API boundary.
- Errors as **`application/problem+json`** (`ProblemDetail`) with stable `type` URIs under `https://localhost:8080/problems/`.
- **OpenAPI** annotations + **Swagger UI** at `/swagger-ui/`.

**Why:** Clear contract for clients; Problem Details improve machine-readable errors over plain text or ad-hoc JSON.

**Trade-off:** DTOs duplicate domain shapes; mappers are hand-written to avoid compile-time coupling from `api` to JPA entities.

---

## 9. Observability: JSON logs and correlation IDs

**Decision:**

- **Logstash Logback encoder** for structured JSON logs.
- **`CorrelationIdFilter`**: read or generate `X-Correlation-Id`, store in **MDC**, echo on the response.

**Why:** Supports distributed tracing patterns in Kubernetes/EKS without a full tracing backend in the exercise.

**Trade-off:** Correlation is request-scoped only; no trace propagation to external systems.

---

## 10. Testing strategy

**Decision:**

| Layer | Approach |
|-------|----------|
| `domain` | JUnit 5 unit tests; **≥ 80%** line coverage on model + `EligibilityEvaluator` (JaCoCo) |
| `application` | One **Testcontainers** PostgreSQL integration test (register + GET) when Docker is available |
| Other layers | Relied on compile-time wiring and manual/REST Client checks |

**Why:** Meets assessment targets where they matter most—business rules—and proves end-to-end persistence once.

**Trade-off:** No dedicated contract tests for OpenAPI; infrastructure adapters are not heavily unit-tested with in-memory databases.

---

## 11. Build, format, and delivery

**Decision:**

- **Gradle** version catalog and multi-module build.
- **Spotless** (Google Java Format) on all subprojects.
- **Docker Compose** for PostgreSQL (+ optional app image via multi-stage `Dockerfile`).

**Why:** Reproducible builds and consistent style for reviewers; Compose lowers onboarding friction.

---

## Alternatives considered (summary)

| Topic | Chosen | Alternative not used |
|-------|--------|----------------------|
| Framework | Micronaut | Spring Boot |
| Concurrency | Virtual threads + blocking JPA | Reactive (R2DBC / Mutiny) |
| Eligibility delivery | In-process async job | Message broker (SQS/Kafka) |
| Delete semantics | Soft delete | Hard delete |
| API errors | RFC 7807 | Custom error envelope only |
| ID type | UUID | Surrogate `BIGSERIAL` |

---

## Document map

| Document | Contents |
|----------|----------|
| [README.md](README.md) | Setup, commands, API table |
| [DECISIONS.md](DECISIONS.md) | This file — architecture overview |
| [docs/adr/001-hexagonal-modules.md](docs/adr/001-hexagonal-modules.md) | Module boundaries |
| [docs/adr/002-async-eligibility.md](docs/adr/002-async-eligibility.md) | Async verification |
| [docs/adr/003-soft-delete-and-email-uniqueness.md](docs/adr/003-soft-delete-and-email-uniqueness.md) | Data retention and uniqueness |
