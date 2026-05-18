# ADR 003: Soft delete and email uniqueness

## Status

Accepted

## Context

Candidates should be removable without losing audit history; email must be unique among active registrations.

## Decision

- `deleted_at` timestamp on `candidates`; queries exclude deleted rows
- Partial unique index `uk_candidates_email_active` on `email WHERE deleted_at IS NULL`
- Repository maps duplicate-key violations to `DuplicateEmailException`

## Consequences

- Re-registration with same email possible after soft delete
- Search and GET never return deleted candidates
- Slightly more complex SQL than hard delete
