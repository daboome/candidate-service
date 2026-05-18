package com.cfa.candidate.api.error;

import io.micronaut.serde.annotation.Serdeable;
import java.net.URI;
import java.util.Map;

@Serdeable
public record ProblemDetail(
    URI type,
    String title,
    int status,
    String detail,
    URI instance,
    Map<String, Object> extensions) {}
