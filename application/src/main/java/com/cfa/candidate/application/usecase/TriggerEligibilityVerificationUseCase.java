package com.cfa.candidate.application.usecase;

import com.cfa.candidate.domain.exception.CandidateNotFoundException;
import com.cfa.candidate.domain.exception.InvalidCandidateStateException;
import com.cfa.candidate.domain.model.Candidate;
import com.cfa.candidate.domain.model.EligibilityStatus;
import com.cfa.candidate.domain.port.CandidateRepository;
import com.cfa.candidate.domain.port.EligibilityVerificationScheduler;
import jakarta.inject.Singleton;
import java.util.UUID;

@Singleton
public class TriggerEligibilityVerificationUseCase {

  private final CandidateRepository candidateRepository;
  private final EligibilityVerificationScheduler verificationScheduler;

  public TriggerEligibilityVerificationUseCase(
      CandidateRepository candidateRepository,
      EligibilityVerificationScheduler verificationScheduler) {
    this.candidateRepository = candidateRepository;
    this.verificationScheduler = verificationScheduler;
  }

  public Candidate execute(UUID id, String verifiedBy) {
    Candidate candidate =
        candidateRepository.findById(id).orElseThrow(() -> new CandidateNotFoundException(id));
    if (candidate.eligibilityStatus() == EligibilityStatus.VERIFYING) {
      throw new InvalidCandidateStateException("Eligibility verification already in progress");
    }
    Candidate verifying = candidate.withStatus(EligibilityStatus.VERIFYING);
    Candidate saved = candidateRepository.save(verifying);
    verificationScheduler.schedule(saved.id(), verifiedBy);
    return saved;
  }
}
