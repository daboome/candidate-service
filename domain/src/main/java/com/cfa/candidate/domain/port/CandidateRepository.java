package com.cfa.candidate.domain.port;

import com.cfa.candidate.domain.model.Candidate;
import com.cfa.candidate.domain.model.EligibilityStatus;
import com.cfa.candidate.domain.model.PageResult;
import com.cfa.candidate.domain.model.ProgramLevel;
import java.util.Optional;
import java.util.UUID;

public interface CandidateRepository {

  Candidate save(Candidate candidate);

  Optional<Candidate> findById(UUID id);

  Optional<Candidate> findActiveByEmail(String email);

  boolean existsActiveByEmail(String email);

  PageResult<Candidate> search(EligibilityStatus status, ProgramLevel program, int page, int size);

  void softDelete(UUID id);
}
