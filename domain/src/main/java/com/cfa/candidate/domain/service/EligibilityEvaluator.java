package com.cfa.candidate.domain.service;

import com.cfa.candidate.domain.model.Candidate;
import com.cfa.candidate.domain.model.EligibilityStatus;
import com.cfa.candidate.domain.model.ExamPass;
import com.cfa.candidate.domain.model.ProgramLevel;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public final class EligibilityEvaluator {

  private static final int MIN_EXPERIENCE_YEARS = 4;
  private static final int EXAM_VALIDITY_YEARS = 5;

  public EligibilityResult evaluate(Candidate candidate) {
    return switch (candidate.selectedProgram()) {
      case LEVEL_I -> evaluateLevelI(candidate);
      case LEVEL_II -> evaluateLevelII(candidate);
      case LEVEL_III -> evaluateLevelIII(candidate);
    };
  }

  private EligibilityResult evaluateLevelI(Candidate candidate) {
    if (candidate.hasBachelorsDegree()) {
      return eligible("Bachelor's degree or higher confirmed.");
    }
    int years = candidate.totalExperienceYears();
    if (years >= MIN_EXPERIENCE_YEARS) {
      return eligible("Professional experience of " + years + " years meets Level I requirement.");
    }
    return ineligible(
        "Level I requires a bachelor's degree or at least "
            + MIN_EXPERIENCE_YEARS
            + " years of professional experience.");
  }

  private EligibilityResult evaluateLevelII(Candidate candidate) {
    return evaluatePrerequisite(
        candidate,
        ProgramLevel.LEVEL_I,
        "Level II requires passing Level I within the last 5 years.");
  }

  private EligibilityResult evaluateLevelIII(Candidate candidate) {
    return evaluatePrerequisite(
        candidate,
        ProgramLevel.LEVEL_II,
        "Level III requires passing Level II within the last 5 years.");
  }

  private EligibilityResult evaluatePrerequisite(
      Candidate candidate, ProgramLevel requiredLevel, String failureMessage) {
    LocalDate cutoff = LocalDate.now().minusYears(EXAM_VALIDITY_YEARS);
    for (ExamPass pass : candidate.examPasses()) {
      if (pass.level() == requiredLevel && !pass.passedAt().isBefore(cutoff)) {
        return eligible(
            "Passed "
                + requiredLevel
                + " on "
                + pass.passedAt()
                + " (within "
                + EXAM_VALIDITY_YEARS
                + "-year window).");
      }
    }
    return ineligible(failureMessage);
  }

  private static EligibilityResult eligible(String reason) {
    return new EligibilityResult(EligibilityStatus.ELIGIBLE, reason);
  }

  private static EligibilityResult ineligible(String reason) {
    return new EligibilityResult(EligibilityStatus.INELIGIBLE, reason);
  }

  public static long yearsBetween(LocalDate start, LocalDate end) {
    return ChronoUnit.YEARS.between(start, end);
  }
}
