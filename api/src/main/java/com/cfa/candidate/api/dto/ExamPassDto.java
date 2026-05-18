package com.cfa.candidate.api.dto;

import com.cfa.candidate.domain.model.ExamPass;
import com.cfa.candidate.domain.model.ProgramLevel;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Serdeable
public record ExamPassDto(@NotNull ProgramLevel level, @NotNull LocalDate passedAt) {

  public ExamPass toDomain() {
    return new ExamPass(level, passedAt);
  }

  public static ExamPassDto fromDomain(ExamPass examPass) {
    return new ExamPassDto(examPass.level(), examPass.passedAt());
  }
}
