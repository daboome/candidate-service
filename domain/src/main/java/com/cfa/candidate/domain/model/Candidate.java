package com.cfa.candidate.domain.model;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record Candidate(
    UUID id,
    String email,
    String firstName,
    String lastName,
    ProgramLevel selectedProgram,
    EligibilityStatus eligibilityStatus,
    List<Education> education,
    List<ProfessionalExperience> experience,
    List<ExamPass> examPasses,
    Instant createdAt,
    Instant updatedAt,
    Instant deletedAt) {

  public boolean isDeleted() {
    return deletedAt != null;
  }

  public int totalExperienceYears() {
    return experience.stream().mapToInt(ProfessionalExperience::years).sum();
  }

  public boolean hasBachelorsDegree() {
    return education.stream()
        .anyMatch(
            e ->
                e.degreeLevel() == DegreeLevel.BACHELORS
                    || e.degreeLevel() == DegreeLevel.MASTERS
                    || e.degreeLevel() == DegreeLevel.DOCTORATE);
  }

  public Candidate withStatus(EligibilityStatus status) {
    return new Candidate(
        id,
        email,
        firstName,
        lastName,
        selectedProgram,
        status,
        education,
        experience,
        examPasses,
        createdAt,
        Instant.now(),
        deletedAt);
  }

  public Candidate withDeletedAt(Instant deletedAt) {
    return new Candidate(
        id,
        email,
        firstName,
        lastName,
        selectedProgram,
        eligibilityStatus,
        education,
        experience,
        examPasses,
        createdAt,
        Instant.now(),
        deletedAt);
  }
}
