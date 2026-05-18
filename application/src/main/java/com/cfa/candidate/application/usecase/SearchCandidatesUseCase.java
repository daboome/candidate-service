package com.cfa.candidate.application.usecase;

import com.cfa.candidate.domain.model.Candidate;
import com.cfa.candidate.domain.model.EligibilityStatus;
import com.cfa.candidate.domain.model.PageResult;
import com.cfa.candidate.domain.model.ProgramLevel;
import com.cfa.candidate.domain.port.CandidateRepository;
import jakarta.inject.Singleton;

@Singleton
public class SearchCandidatesUseCase {

  private static final int MAX_PAGE_SIZE = 100;
  private static final int DEFAULT_PAGE_SIZE = 20;

  private final CandidateRepository candidateRepository;

  public SearchCandidatesUseCase(CandidateRepository candidateRepository) {
    this.candidateRepository = candidateRepository;
  }

  public PageResult<Candidate> execute(
      EligibilityStatus status, ProgramLevel program, int page, int size) {
    int safePage = Math.max(page, 0);
    int safeSize = size <= 0 ? DEFAULT_PAGE_SIZE : Math.min(size, MAX_PAGE_SIZE);
    return candidateRepository.search(status, program, safePage, safeSize);
  }
}
