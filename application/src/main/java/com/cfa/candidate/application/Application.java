package com.cfa.candidate.application;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
    info =
        @Info(
            title = "Candidate Registration API",
            version = "1.0",
            description = "REST API for candidate registration and eligibility verification"))
public class Application {

  public static void main(String[] args) {
    Micronaut.build(args).packages("com.cfa.candidate").start();
  }
}
