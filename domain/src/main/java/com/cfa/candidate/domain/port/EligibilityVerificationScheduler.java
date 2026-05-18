package com.cfa.candidate.domain.port;

import java.util.UUID;

public interface EligibilityVerificationScheduler {

  void schedule(UUID candidateId, String verifiedBy);
}
