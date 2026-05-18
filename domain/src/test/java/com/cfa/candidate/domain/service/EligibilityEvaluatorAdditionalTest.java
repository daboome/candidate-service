package com.cfa.candidate.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.cfa.candidate.domain.model.Candidate;
import com.cfa.candidate.domain.model.DegreeLevel;
import com.cfa.candidate.domain.model.Education;
import com.cfa.candidate.domain.model.EligibilityStatus;
import com.cfa.candidate.domain.model.ProfessionalExperience;
import com.cfa.candidate.domain.model.ProgramLevel;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class EligibilityEvaluatorAdditionalTest {

  private final EligibilityEvaluator evaluator = new EligibilityEvaluator();

  @Test
  void levelI_eligibleWithDoctorate() {
    var candidate =
        candidate(
            ProgramLevel.LEVEL_I,
            List.of(new Education(DegreeLevel.DOCTORATE, "Institute", 2015)),
            List.of(),
            List.of());

    assertEquals(EligibilityStatus.ELIGIBLE, evaluator.evaluate(candidate).status());
  }

  @Test
  void totalExperience_sumsMultipleRoles() {
    var candidate =
        candidate(
            ProgramLevel.LEVEL_I,
            List.of(),
            List.of(
                new ProfessionalExperience("A", "Role", LocalDate.now().minusYears(2), null),
                new ProfessionalExperience("B", "Role", LocalDate.now().minusYears(3), null)),
            List.of());

    assertEquals(EligibilityStatus.ELIGIBLE, evaluator.evaluate(candidate).status());
  }

  @Test
  void yearsBetween_helper() {
    assertEquals(
        5, EligibilityEvaluator.yearsBetween(LocalDate.of(2018, 1, 1), LocalDate.of(2023, 1, 1)));
  }

  private static Candidate candidate(
      ProgramLevel program,
      List<Education> education,
      List<ProfessionalExperience> experience,
      List<com.cfa.candidate.domain.model.ExamPass> examPasses) {
    return new Candidate(
        UUID.randomUUID(),
        "x@y.com",
        "A",
        "B",
        program,
        EligibilityStatus.PENDING,
        education,
        experience,
        examPasses,
        Instant.now(),
        Instant.now(),
        null);
  }
}
