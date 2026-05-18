package com.cfa.candidate.domain.service;

import com.cfa.candidate.domain.model.EligibilityStatus;

public record EligibilityResult(EligibilityStatus status, String reason) {}
