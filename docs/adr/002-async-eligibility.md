# ADR 002: Asynchronous eligibility verification

## Status

Accepted

## Context

Eligibility evaluation may involve rule checks and audit writes; the API should not block clients for long-running work.

## Decision

- `PUT /eligibility` sets status to `VERIFYING` and schedules work on a **virtual-thread executor**
- Worker runs `EligibilityEvaluator`, updates candidate status, and appends an audit row
- Clients poll `GET /candidates/{id}` for final `ELIGIBLE` / `INELIGIBLE`

## Consequences

- Simple scaling story on Java 21 without reactive stack
- Eventual consistency; callers must handle in-progress state
- Failures mark candidate `INELIGIBLE` and log error (production would use retry/DLQ)
