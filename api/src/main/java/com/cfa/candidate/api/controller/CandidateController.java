package com.cfa.candidate.api.controller;

import com.cfa.candidate.api.dto.CandidatePageResponse;
import com.cfa.candidate.api.dto.CandidateResponse;
import com.cfa.candidate.api.dto.EducationDto;
import com.cfa.candidate.api.dto.EligibilityVerificationRequest;
import com.cfa.candidate.api.dto.ExamPassDto;
import com.cfa.candidate.api.dto.ProfessionalExperienceDto;
import com.cfa.candidate.api.dto.RegisterCandidateRequest;
import com.cfa.candidate.application.usecase.DeleteCandidateUseCase;
import com.cfa.candidate.application.usecase.GetCandidateUseCase;
import com.cfa.candidate.application.usecase.RegisterCandidateCommand;
import com.cfa.candidate.application.usecase.RegisterCandidateUseCase;
import com.cfa.candidate.application.usecase.SearchCandidatesUseCase;
import com.cfa.candidate.application.usecase.TriggerEligibilityVerificationUseCase;
import com.cfa.candidate.domain.model.EligibilityStatus;
import com.cfa.candidate.domain.model.ProgramLevel;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.annotation.Status;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@Controller("/api/v1/candidates")
@Tag(name = "Candidates", description = "Candidate registration and eligibility API")
public class CandidateController {

  private final RegisterCandidateUseCase registerCandidateUseCase;
  private final GetCandidateUseCase getCandidateUseCase;
  private final SearchCandidatesUseCase searchCandidatesUseCase;
  private final DeleteCandidateUseCase deleteCandidateUseCase;
  private final TriggerEligibilityVerificationUseCase triggerEligibilityVerificationUseCase;

  public CandidateController(
      RegisterCandidateUseCase registerCandidateUseCase,
      GetCandidateUseCase getCandidateUseCase,
      SearchCandidatesUseCase searchCandidatesUseCase,
      DeleteCandidateUseCase deleteCandidateUseCase,
      TriggerEligibilityVerificationUseCase triggerEligibilityVerificationUseCase) {
    this.registerCandidateUseCase = registerCandidateUseCase;
    this.getCandidateUseCase = getCandidateUseCase;
    this.searchCandidatesUseCase = searchCandidatesUseCase;
    this.deleteCandidateUseCase = deleteCandidateUseCase;
    this.triggerEligibilityVerificationUseCase = triggerEligibilityVerificationUseCase;
  }

  @Post
  @Status(HttpStatus.CREATED)
  @Operation(summary = "Register a new candidate")
  @ApiResponse(responseCode = "201", description = "Candidate created")
  public CandidateResponse register(@Valid @Body RegisterCandidateRequest request) {
    var candidate =
        registerCandidateUseCase.execute(
            new RegisterCandidateCommand(
                request.email(),
                request.firstName(),
                request.lastName(),
                request.selectedProgram(),
                request.education() == null
                    ? List.of()
                    : request.education().stream().map(EducationDto::toDomain).toList(),
                request.experience() == null
                    ? List.of()
                    : request.experience().stream()
                        .map(ProfessionalExperienceDto::toDomain)
                        .toList(),
                request.examPasses() == null
                    ? List.of()
                    : request.examPasses().stream().map(ExamPassDto::toDomain).toList()));
    return CandidateResponse.fromDomain(candidate);
  }

  @Get("/{id}")
  @Operation(summary = "Get candidate by ID")
  public CandidateResponse get(@PathVariable UUID id) {
    return CandidateResponse.fromDomain(getCandidateUseCase.execute(id));
  }

  @Put("/{id}/eligibility")
  @Status(HttpStatus.ACCEPTED)
  @Operation(summary = "Trigger eligibility verification")
  public CandidateResponse triggerEligibility(
      @PathVariable UUID id,
      @Header(value = "X-Actor-Id", defaultValue = "system") String actorId,
      @Body @Valid EligibilityVerificationRequest request) {
    String verifiedBy =
        request != null && request.verifiedBy() != null ? request.verifiedBy() : actorId;
    return CandidateResponse.fromDomain(
        triggerEligibilityVerificationUseCase.execute(id, verifiedBy));
  }

  @Get
  @Operation(summary = "Search candidates")
  public CandidatePageResponse search(
      @QueryValue(value = "status", defaultValue = "") String status,
      @QueryValue(value = "program", defaultValue = "") String program,
      @QueryValue(defaultValue = "0") int page,
      @QueryValue(defaultValue = "20") int size) {
    EligibilityStatus statusFilter =
        status == null || status.isBlank() ? null : EligibilityStatus.valueOf(status);
    ProgramLevel programFilter =
        program == null || program.isBlank() ? null : ProgramLevel.valueOf(program);
    return CandidatePageResponse.fromDomain(
        searchCandidatesUseCase.execute(statusFilter, programFilter, page, size));
  }

  @Delete("/{id}")
  @Status(HttpStatus.NO_CONTENT)
  @Operation(summary = "Soft-delete a candidate")
  public HttpResponse<Void> delete(@PathVariable UUID id) {
    deleteCandidateUseCase.execute(id);
    return HttpResponse.noContent();
  }
}
