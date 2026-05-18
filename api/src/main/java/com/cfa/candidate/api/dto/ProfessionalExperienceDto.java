package com.cfa.candidate.api.dto;

import com.cfa.candidate.domain.model.ProfessionalExperience;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Serdeable
public record ProfessionalExperienceDto(
    @NotBlank String employer,
    @NotBlank String role,
    @NotNull LocalDate startDate,
    LocalDate endDate) {

  public ProfessionalExperience toDomain() {
    return new ProfessionalExperience(employer, role, startDate, endDate);
  }

  public static ProfessionalExperienceDto fromDomain(ProfessionalExperience experience) {
    return new ProfessionalExperienceDto(
        experience.employer(), experience.role(), experience.startDate(), experience.endDate());
  }
}
