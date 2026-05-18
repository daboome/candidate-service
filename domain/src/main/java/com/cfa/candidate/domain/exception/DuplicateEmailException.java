package com.cfa.candidate.domain.exception;

public class DuplicateEmailException extends DomainException {

  public DuplicateEmailException(String email) {
    super("Active candidate already exists with email: " + email);
  }
}
