# ADR 001: Hexagonal multi-module layout

## Status

Accepted

## Context

The assessment requires Clean/Hexagonal architecture with Gradle modules `api`, `domain`, `infrastructure`, and `application`.

## Decision

- **domain**: pure Java, no framework dependencies
- **application**: use cases and Micronaut entrypoint
- **infrastructure**: JPA, Liquibase, async eligibility worker
- **api**: HTTP adapters; loaded at runtime by `application` to avoid circular Gradle dependencies

## Consequences

- Domain rules remain testable without containers
- Infrastructure can be swapped (e.g. in-memory repos for tests) by binding port implementations
- `application` orchestrates composition while `api` stays thin
