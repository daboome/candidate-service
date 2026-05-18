package com.cfa.candidate.infrastructure.persistence.mapper;

import com.cfa.candidate.domain.model.Candidate;
import com.cfa.candidate.domain.model.DegreeLevel;
import com.cfa.candidate.domain.model.Education;
import com.cfa.candidate.domain.model.EligibilityStatus;
import com.cfa.candidate.domain.model.ExamPass;
import com.cfa.candidate.domain.model.ProfessionalExperience;
import com.cfa.candidate.domain.model.ProgramLevel;
import com.cfa.candidate.infrastructure.persistence.entity.CandidateEntity;
import com.cfa.candidate.infrastructure.persistence.entity.EducationEntity;
import com.cfa.candidate.infrastructure.persistence.entity.ExamPassEntity;
import com.cfa.candidate.infrastructure.persistence.entity.ProfessionalExperienceEntity;
import java.util.UUID;
import java.util.stream.Collectors;

public final class CandidateMapper {

  private CandidateMapper() {}

  public static Candidate toDomain(CandidateEntity entity) {
    return new Candidate(
        entity.getId(),
        entity.getEmail(),
        entity.getFirstName(),
        entity.getLastName(),
        ProgramLevel.valueOf(entity.getSelectedProgram()),
        EligibilityStatus.valueOf(entity.getEligibilityStatus()),
        entity.getEducation().stream()
            .map(CandidateMapper::toEducation)
            .collect(Collectors.toList()),
        entity.getExperience().stream()
            .map(CandidateMapper::toExperience)
            .collect(Collectors.toList()),
        entity.getExamPasses().stream()
            .map(CandidateMapper::toExamPass)
            .collect(Collectors.toList()),
        entity.getCreatedAt(),
        entity.getUpdatedAt(),
        entity.getDeletedAt());
  }

  public static CandidateEntity toEntity(Candidate candidate) {
    CandidateEntity entity = new CandidateEntity();
    entity.setId(candidate.id());
    entity.setEmail(candidate.email());
    entity.setFirstName(candidate.firstName());
    entity.setLastName(candidate.lastName());
    entity.setSelectedProgram(candidate.selectedProgram().name());
    entity.setEligibilityStatus(candidate.eligibilityStatus().name());
    entity.setCreatedAt(candidate.createdAt());
    entity.setUpdatedAt(candidate.updatedAt());
    entity.setDeletedAt(candidate.deletedAt());

    entity.setEducation(
        candidate.education().stream()
            .map(e -> toEducationEntity(entity, e))
            .collect(Collectors.toList()));
    entity.setExperience(
        candidate.experience().stream()
            .map(e -> toExperienceEntity(entity, e))
            .collect(Collectors.toList()));
    entity.setExamPasses(
        candidate.examPasses().stream()
            .map(e -> toExamPassEntity(entity, e))
            .collect(Collectors.toList()));
    return entity;
  }

  public static void updateEntity(CandidateEntity entity, Candidate candidate) {
    entity.setEmail(candidate.email());
    entity.setFirstName(candidate.firstName());
    entity.setLastName(candidate.lastName());
    entity.setSelectedProgram(candidate.selectedProgram().name());
    entity.setEligibilityStatus(candidate.eligibilityStatus().name());
    entity.setUpdatedAt(candidate.updatedAt());
    entity.setDeletedAt(candidate.deletedAt());

    entity.getEducation().clear();
    entity
        .getEducation()
        .addAll(
            candidate.education().stream()
                .map(e -> toEducationEntity(entity, e))
                .collect(Collectors.toList()));
    entity.getExperience().clear();
    entity
        .getExperience()
        .addAll(
            candidate.experience().stream()
                .map(e -> toExperienceEntity(entity, e))
                .collect(Collectors.toList()));
    entity.getExamPasses().clear();
    entity
        .getExamPasses()
        .addAll(
            candidate.examPasses().stream()
                .map(e -> toExamPassEntity(entity, e))
                .collect(Collectors.toList()));
  }

  private static Education toEducation(EducationEntity entity) {
    return new Education(
        DegreeLevel.valueOf(entity.getDegreeLevel()),
        entity.getInstitution(),
        entity.getGraduationYear());
  }

  private static EducationEntity toEducationEntity(CandidateEntity candidate, Education education) {
    EducationEntity entity = new EducationEntity();
    entity.setId(UUID.randomUUID());
    entity.setCandidate(candidate);
    entity.setDegreeLevel(education.degreeLevel().name());
    entity.setInstitution(education.institution());
    entity.setGraduationYear(education.graduationYear());
    return entity;
  }

  private static ProfessionalExperience toExperience(ProfessionalExperienceEntity entity) {
    return new ProfessionalExperience(
        entity.getEmployer(), entity.getRole(), entity.getStartDate(), entity.getEndDate());
  }

  private static ProfessionalExperienceEntity toExperienceEntity(
      CandidateEntity candidate, ProfessionalExperience experience) {
    ProfessionalExperienceEntity entity = new ProfessionalExperienceEntity();
    entity.setId(UUID.randomUUID());
    entity.setCandidate(candidate);
    entity.setEmployer(experience.employer());
    entity.setRole(experience.role());
    entity.setStartDate(experience.startDate());
    entity.setEndDate(experience.endDate());
    return entity;
  }

  private static ExamPass toExamPass(ExamPassEntity entity) {
    return new ExamPass(ProgramLevel.valueOf(entity.getLevel()), entity.getPassedAt());
  }

  private static ExamPassEntity toExamPassEntity(CandidateEntity candidate, ExamPass examPass) {
    ExamPassEntity entity = new ExamPassEntity();
    entity.setId(UUID.randomUUID());
    entity.setCandidate(candidate);
    entity.setLevel(examPass.level().name());
    entity.setPassedAt(examPass.passedAt());
    return entity;
  }
}
