package com.cfa.candidate.api.dto;

import com.cfa.candidate.domain.model.DegreeLevel;
import com.cfa.candidate.domain.model.Education;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Serdeable
public record EducationDto(
    @NotNull DegreeLevel degreeLevel, @NotBlank String institution, Integer graduationYear) {

  public Education toDomain() {
    return new Education(degreeLevel, institution, graduationYear);
  }

  public static EducationDto fromDomain(Education education) {
    return new EducationDto(
        education.degreeLevel(), education.institution(), education.graduationYear());
  }
}
