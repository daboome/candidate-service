package com.cfa.candidate.api.dto;

import com.cfa.candidate.domain.model.ProgramLevel;
import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Serdeable
@Schema(name = "RegisterCandidateRequest")
public record RegisterCandidateRequest(
    @NotBlank @Email String email,
    @NotBlank String firstName,
    @NotBlank String lastName,
    @NotNull ProgramLevel selectedProgram,
    @Valid List<EducationDto> education,
    @Valid List<ProfessionalExperienceDto> experience,
    @Valid List<ExamPassDto> examPasses) {}
