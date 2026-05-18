package com.cfa.candidate.application.usecase;

import com.cfa.candidate.domain.exception.CandidateNotFoundException;
import com.cfa.candidate.domain.model.Candidate;
import com.cfa.candidate.domain.port.CandidateRepository;
import jakarta.inject.Singleton;
import java.util.UUID;

@Singleton
public class GetCandidateUseCase {

  private final CandidateRepository candidateRepository;

  public GetCandidateUseCase(CandidateRepository candidateRepository) {
    this.candidateRepository = candidateRepository;
  }

  public Candidate execute(UUID id) {
    return candidateRepository.findById(id).orElseThrow(() -> new CandidateNotFoundException(id));
  }
}
