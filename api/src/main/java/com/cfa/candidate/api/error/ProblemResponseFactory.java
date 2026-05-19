package com.cfa.candidate.api.error;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import java.util.Map;

public final class ProblemResponseFactory {

  public static final MediaType PROBLEM_JSON = MediaType.of("application/problem+json");

  private ProblemResponseFactory() {}

  public static HttpResponse<ProblemDetail> create(
      HttpRequest<?> request,
      String type,
      String title,
      HttpStatus status,
      String detail,
      Map<String, Object> extensions) {
    return HttpResponse.status(status)
        .contentType(PROBLEM_JSON)
        .body(ProblemDetailFactory.of(request, type, title, status.getCode(), detail, extensions));
  }

  public static HttpResponse<ProblemDetail> create(
      HttpRequest<?> request, String type, String title, HttpStatus status, String detail) {
    return create(request, type, title, status, detail, Map.of());
  }
}
