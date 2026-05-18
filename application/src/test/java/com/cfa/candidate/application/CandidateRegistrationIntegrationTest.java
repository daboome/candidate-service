package com.cfa.candidate.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import org.testcontainers.DockerClientFactory;
import org.testcontainers.containers.PostgreSQLContainer;

@EnabledIf("dockerAvailable")
@MicronautTest(environments = "test", transactional = false)
class CandidateRegistrationIntegrationTest implements TestPropertyProvider {

  private static final Argument<Map<String, Object>> MAP_ARGUMENT =
      Argument.mapOf(String.class, Object.class);

  @SuppressWarnings("resource")
  static final PostgreSQLContainer<?> POSTGRES =
      new PostgreSQLContainer<>("postgres:16-alpine")
          .withDatabaseName("candidates")
          .withUsername("candidate")
          .withPassword("candidate");

  static {
    if (dockerAvailable()) {
      POSTGRES.start();
    }
  }

  @Inject
  @Client("/")
  HttpClient client;

  @Override
  public Map<String, String> getProperties() {
    if (!dockerAvailable()) {
      return Map.of();
    }
    return Map.of(
        "datasources.default.url", POSTGRES.getJdbcUrl(),
        "datasources.default.username", POSTGRES.getUsername(),
        "datasources.default.password", POSTGRES.getPassword());
  }

  static boolean dockerAvailable() {
    try {
      return DockerClientFactory.instance().isDockerAvailable();
    } catch (Exception ex) {
      return false;
    }
  }

  @Test
  void registerAndRetrieveCandidate() {
    String email = "integration-" + UUID.randomUUID() + "@example.com";
    Map<String, Object> payload =
        Map.of(
            "email",
            email,
            "firstName",
            "Integration",
            "lastName",
            "Test",
            "selectedProgram",
            "LEVEL_I",
            "education",
            List.of(
                Map.of(
                    "degreeLevel",
                    "BACHELORS",
                    "institution",
                    "Test University",
                    "graduationYear",
                    2019)),
            "experience",
            List.of(),
            "examPasses",
            List.of());

    Map<String, Object> created =
        client.toBlocking().retrieve(HttpRequest.POST("/api/v1/candidates", payload), MAP_ARGUMENT);

    assertNotNull(created.get("id"));
    String id = created.get("id").toString();

    Map<String, Object> fetched =
        client.toBlocking().retrieve(HttpRequest.GET("/api/v1/candidates/" + id), MAP_ARGUMENT);

    assertEquals(email, fetched.get("email"));
    assertEquals("PENDING", fetched.get("eligibilityStatus"));
    assertEquals("LEVEL_I", fetched.get("selectedProgram"));
  }
}
