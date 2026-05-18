package com.cfa.candidate.api.dto;

import com.cfa.candidate.domain.model.Candidate;
import com.cfa.candidate.domain.model.EligibilityStatus;
import com.cfa.candidate.domain.model.ProgramLevel;
import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Serdeable
@Schema(name = "CandidateResponse")
public record CandidateResponse(
    UUID id,
    String email,
    String firstName,
    String lastName,
    ProgramLevel selectedProgram,
    EligibilityStatus eligibilityStatus,
    List<EducationDto> education,
    List<ProfessionalExperienceDto> experience,
    List<ExamPassDto> examPasses,
    Instant createdAt,
    Instant updatedAt) {

  public static CandidateResponse fromDomain(Candidate candidate) {
    return new CandidateResponse(
        candidate.id(),
        candidate.email(),
        candidate.firstName(),
        candidate.lastName(),
        candidate.selectedProgram(),
        candidate.eligibilityStatus(),
        candidate.education().stream().map(EducationDto::fromDomain).toList(),
        candidate.experience().stream().map(ProfessionalExperienceDto::fromDomain).toList(),
        candidate.examPasses().stream().map(ExamPassDto::fromDomain).toList(),
        candidate.createdAt(),
        candidate.updatedAt());
  }
}
