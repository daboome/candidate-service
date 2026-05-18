package com.cfa.candidate.application.usecase;

import com.cfa.candidate.domain.port.CandidateRepository;
import jakarta.inject.Singleton;
import java.util.UUID;

@Singleton
public class DeleteCandidateUseCase {

  private final CandidateRepository candidateRepository;

  public DeleteCandidateUseCase(CandidateRepository candidateRepository) {
    this.candidateRepository = candidateRepository;
  }

  public void execute(UUID id) {
    candidateRepository.softDelete(id);
  }
}
