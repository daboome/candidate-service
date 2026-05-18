package com.cfa.candidate.api.dto;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;

@Serdeable
public record EligibilityVerificationRequest(@NotBlank String verifiedBy) {}
