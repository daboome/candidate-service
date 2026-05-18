package com.cfa.candidate.domain.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class DomainExceptionsTest {

  @Test
  void duplicateEmailException_containsEmail() {
    var ex = new DuplicateEmailException("user@cfa.org");
    assertTrue(ex.getMessage().contains("user@cfa.org"));
  }

  @Test
  void candidateNotFoundException_containsId() {
    UUID id = UUID.randomUUID();
    var ex = new CandidateNotFoundException(id);
    assertEquals("Candidate not found: " + id, ex.getMessage());
  }

  @Test
  void invalidCandidateStateException_usesMessage() {
    var ex = new InvalidCandidateStateException("bad state");
    assertEquals("bad state", ex.getMessage());
  }
}
