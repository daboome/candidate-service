package com.cfa.candidate.domain.exception;

import java.util.UUID;

public class CandidateNotFoundException extends DomainException {

  public CandidateNotFoundException(UUID id) {
    super("Candidate not found: " + id);
  }
}
