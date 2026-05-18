package com.cfa.candidate.api.error;

import io.micronaut.http.HttpRequest;
import java.net.URI;
import java.util.Map;

public final class ProblemDetailFactory {

  private static final URI BASE_TYPE = URI.create("https://api.cfa.candidate/problems/");

  private ProblemDetailFactory() {}

  public static ProblemDetail of(
      HttpRequest<?> request,
      String problem,
      String title,
      int status,
      String detail,
      Map<String, Object> extensions) {
    return new ProblemDetail(
        BASE_TYPE.resolve(problem),
        title,
        status,
        detail,
        URI.create(request.getUri().toString()),
        extensions);
  }
}
