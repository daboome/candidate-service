package com.cfa.candidate.domain.model;

import java.time.Instant;
import java.util.UUID;

public record EligibilityAuditEntry(
    UUID id,
    UUID candidateId,
    String verifiedBy,
    Instant verifiedAt,
    EligibilityStatus decision,
    String reason) {}
