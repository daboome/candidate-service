package com.cfa.candidate.infrastructure.eligibility;

import com.cfa.candidate.domain.port.EligibilityVerificationScheduler;
import jakarta.inject.Singleton;
import java.util.UUID;

@Singleton
public class MicronautEligibilityVerificationScheduler implements EligibilityVerificationScheduler {

  private final EligibilityVerificationJob job;

  public MicronautEligibilityVerificationScheduler(EligibilityVerificationJob job) {
    this.job = job;
  }

  @Override
  public void schedule(UUID candidateId, String verifiedBy) {
    job.runAsync(candidateId, verifiedBy);
  }
}
