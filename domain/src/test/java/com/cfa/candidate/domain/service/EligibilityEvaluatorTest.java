package com.cfa.candidate.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.cfa.candidate.domain.model.Candidate;
import com.cfa.candidate.domain.model.DegreeLevel;
import com.cfa.candidate.domain.model.Education;
import com.cfa.candidate.domain.model.EligibilityStatus;
import com.cfa.candidate.domain.model.ExamPass;
import com.cfa.candidate.domain.model.ProfessionalExperience;
import com.cfa.candidate.domain.model.ProgramLevel;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EligibilityEvaluatorTest {

  private EligibilityEvaluator evaluator;

  @BeforeEach
  void setUp() {
    evaluator = new EligibilityEvaluator();
  }

  @Test
  void levelI_eligibleWithBachelorsDegree() {
    var candidate =
        candidate(
            ProgramLevel.LEVEL_I,
            List.of(new Education(DegreeLevel.BACHELORS, "State U", 2020)),
            List.of(),
            List.of());

    var result = evaluator.evaluate(candidate);

    assertEquals(EligibilityStatus.ELIGIBLE, result.status());
  }

  @Test
  void levelI_eligibleWithExactlyFourYearsExperience() {
    LocalDate start = LocalDate.now().minusYears(4);
    var candidate =
        candidate(
            ProgramLevel.LEVEL_I,
            List.of(),
            List.of(new ProfessionalExperience("Corp", "Analyst", start, null)),
            List.of());

    var result = evaluator.evaluate(candidate);

    assertEquals(EligibilityStatus.ELIGIBLE, result.status());
  }

  @Test
  void levelI_ineligibleWithThreeYearsExperience() {
    LocalDate start = LocalDate.now().minusYears(3);
    var candidate =
        candidate(
            ProgramLevel.LEVEL_I,
            List.of(),
            List.of(new ProfessionalExperience("Corp", "Analyst", start, null)),
            List.of());

    var result = evaluator.evaluate(candidate);

    assertEquals(EligibilityStatus.INELIGIBLE, result.status());
  }

  @Test
  void levelII_eligibleWhenLevelIPassedWithinFiveYears() {
    var candidate =
        candidate(
            ProgramLevel.LEVEL_II,
            List.of(),
            List.of(),
            List.of(new ExamPass(ProgramLevel.LEVEL_I, LocalDate.now().minusYears(4))));

    var result = evaluator.evaluate(candidate);

    assertEquals(EligibilityStatus.ELIGIBLE, result.status());
  }

  @Test
  void levelII_ineligibleWhenLevelIPassedMoreThanFiveYearsAgo() {
    var candidate =
        candidate(
            ProgramLevel.LEVEL_II,
            List.of(),
            List.of(),
            List.of(new ExamPass(ProgramLevel.LEVEL_I, LocalDate.now().minusYears(6))));

    var result = evaluator.evaluate(candidate);

    assertEquals(EligibilityStatus.INELIGIBLE, result.status());
  }

  @Test
  void levelII_ineligibleWithoutLevelIPass() {
    var result =
        evaluator.evaluate(candidate(ProgramLevel.LEVEL_II, List.of(), List.of(), List.of()));

    assertEquals(EligibilityStatus.INELIGIBLE, result.status());
  }

  @Test
  void levelIII_eligibleWhenLevelIIPassedWithinFiveYears() {
    var candidate =
        candidate(
            ProgramLevel.LEVEL_III,
            List.of(),
            List.of(),
            List.of(new ExamPass(ProgramLevel.LEVEL_II, LocalDate.now().minusYears(1))));

    var result = evaluator.evaluate(candidate);

    assertEquals(EligibilityStatus.ELIGIBLE, result.status());
  }

  @Test
  void levelIII_ineligibleWhenOnlyLevelIPassed() {
    var candidate =
        candidate(
            ProgramLevel.LEVEL_III,
            List.of(),
            List.of(),
            List.of(new ExamPass(ProgramLevel.LEVEL_I, LocalDate.now().minusYears(1))));

    var result = evaluator.evaluate(candidate);

    assertEquals(EligibilityStatus.INELIGIBLE, result.status());
  }

  private static Candidate candidate(
      ProgramLevel program,
      List<Education> education,
      List<ProfessionalExperience> experience,
      List<ExamPass> examPasses) {
    return new Candidate(
        UUID.randomUUID(),
        "test@example.com",
        "Jane",
        "Doe",
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
