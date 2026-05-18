package com.cfa.candidate.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class CandidateTest {

  @Test
  void hasBachelorsDegree_trueForMasters() {
    var candidate = candidate(List.of(new Education(DegreeLevel.MASTERS, "Uni", 2020)));

    assertTrue(candidate.hasBachelorsDegree());
  }

  @Test
  void hasBachelorsDegree_falseForAssociateOnly() {
    var candidate = candidate(List.of(new Education(DegreeLevel.ASSOCIATE, "College", 2020)));

    assertFalse(candidate.hasBachelorsDegree());
  }

  @Test
  void withStatus_updatesEligibilityAndTimestamp() {
    var original = candidate(List.of());
    var updated = original.withStatus(EligibilityStatus.ELIGIBLE);

    assertEquals(EligibilityStatus.ELIGIBLE, updated.eligibilityStatus());
    assertTrue(
        updated.updatedAt().isAfter(original.updatedAt())
            || updated.updatedAt().equals(original.updatedAt()));
  }

  @Test
  void isDeleted_trueWhenTimestampPresent() {
    Instant now = Instant.now();
    var candidate =
        new Candidate(
            UUID.randomUUID(),
            "a@b.com",
            "A",
            "B",
            ProgramLevel.LEVEL_I,
            EligibilityStatus.PENDING,
            List.of(),
            List.of(),
            List.of(),
            now,
            now,
            now);

    assertTrue(candidate.isDeleted());
  }

  private static Candidate candidate(List<Education> education) {
    return new Candidate(
        UUID.randomUUID(),
        "a@b.com",
        "A",
        "B",
        ProgramLevel.LEVEL_I,
        EligibilityStatus.PENDING,
        education,
        List.of(),
        List.of(),
        Instant.now(),
        Instant.now(),
        null);
  }
}
