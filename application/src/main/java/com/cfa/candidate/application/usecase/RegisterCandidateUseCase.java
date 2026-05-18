package com.cfa.candidate.application.usecase;

import com.cfa.candidate.domain.exception.DuplicateEmailException;
import com.cfa.candidate.domain.model.Candidate;
import com.cfa.candidate.domain.model.EligibilityStatus;
import com.cfa.candidate.domain.port.CandidateRepository;
import jakarta.inject.Singleton;
import java.time.Instant;
import java.util.UUID;

@Singleton
public class RegisterCandidateUseCase {

  private final CandidateRepository candidateRepository;

  public RegisterCandidateUseCase(CandidateRepository candidateRepository) {
    this.candidateRepository = candidateRepository;
  }

  public Candidate execute(RegisterCandidateCommand command) {
    if (candidateRepository.existsActiveByEmail(command.email())) {
      throw new DuplicateEmailException(command.email());
    }
    Instant now = Instant.now();
    Candidate candidate =
        new Candidate(
            UUID.randomUUID(),
            command.email().trim().toLowerCase(),
            command.firstName(),
            command.lastName(),
            command.selectedProgram(),
            EligibilityStatus.PENDING,
            command.education(),
            command.experience(),
            command.examPasses(),
            now,
            now,
            null);
    return candidateRepository.save(candidate);
  }
}
