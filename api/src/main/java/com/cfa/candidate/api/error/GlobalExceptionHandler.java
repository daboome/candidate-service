package com.cfa.candidate.api.error;

import com.cfa.candidate.domain.exception.CandidateNotFoundException;
import com.cfa.candidate.domain.exception.DomainException;
import com.cfa.candidate.domain.exception.DuplicateEmailException;
import com.cfa.candidate.domain.exception.InvalidCandidateStateException;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.exceptions.HttpStatusException;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;
import jakarta.validation.ConstraintViolationException;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
@Produces("application/problem+json")
public class GlobalExceptionHandler
    implements ExceptionHandler<Throwable, HttpResponse<ProblemDetail>> {

  private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @Override
  public HttpResponse<ProblemDetail> handle(HttpRequest request, Throwable exception) {
    if (exception instanceof DuplicateEmailException dup) {
      return ProblemResponseFactory.create(
          request, "duplicate-email", "Duplicate email", HttpStatus.CONFLICT, dup.getMessage());
    }
    if (exception instanceof CandidateNotFoundException notFound) {
      return ProblemResponseFactory.create(
          request, "not-found", "Candidate not found", HttpStatus.NOT_FOUND, notFound.getMessage());
    }
    if (exception instanceof InvalidCandidateStateException invalid) {
      return ProblemResponseFactory.create(
          request,
          "invalid-state",
          "Invalid candidate state",
          HttpStatus.UNPROCESSABLE_ENTITY,
          invalid.getMessage());
    }
    if (exception instanceof ConstraintViolationException violation) {
      String detail =
          violation.getConstraintViolations().stream()
              .map(v -> v.getPropertyPath() + ": " + v.getMessage())
              .collect(Collectors.joining("; "));
      return ProblemResponseFactory.create(
          request,
          "validation-error",
          "Validation failed",
          HttpStatus.BAD_REQUEST,
          detail,
          Map.of("violations", detail));
    }
    if (exception instanceof DomainException domain) {
      return ProblemResponseFactory.create(
          request,
          "domain-error",
          "Business rule violation",
          HttpStatus.UNPROCESSABLE_ENTITY,
          domain.getMessage());
    }
    if (exception instanceof HttpStatusException http) {
      return ProblemResponseFactory.create(
          request, "http-error", http.getStatus().getReason(), http.getStatus(), http.getMessage());
    }
    LOG.error("Unhandled exception for {} {}", request.getMethod(), request.getUri(), exception);
    return ProblemResponseFactory.create(
        request,
        "internal-error",
        "Internal server error",
        HttpStatus.INTERNAL_SERVER_ERROR,
        "An unexpected error occurred");
  }
}
