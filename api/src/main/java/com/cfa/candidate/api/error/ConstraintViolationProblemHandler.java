package com.cfa.candidate.api.error;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import io.micronaut.validation.exceptions.ConstraintExceptionHandler;
import jakarta.inject.Singleton;
import jakarta.validation.ConstraintViolationException;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Replaces Micronaut's default {@link ConstraintExceptionHandler} so {@code @Valid} failures return
 * RFC 7807 problem details instead of the framework's default JSON error envelope.
 */
@Singleton
@Replaces(ConstraintExceptionHandler.class)
@Produces("application/problem+json")
public class ConstraintViolationProblemHandler
    implements ExceptionHandler<ConstraintViolationException, HttpResponse<ProblemDetail>> {

  @Override
  public HttpResponse<ProblemDetail> handle(
      HttpRequest request, ConstraintViolationException exception) {
    String detail =
        exception.getConstraintViolations().stream()
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
}
