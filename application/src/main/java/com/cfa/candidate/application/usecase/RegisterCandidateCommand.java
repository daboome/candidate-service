package com.cfa.candidate.application.usecase;

import com.cfa.candidate.domain.model.Education;
import com.cfa.candidate.domain.model.ExamPass;
import com.cfa.candidate.domain.model.ProfessionalExperience;
import com.cfa.candidate.domain.model.ProgramLevel;
import java.util.List;

public record RegisterCandidateCommand(
    String email,
    String firstName,
    String lastName,
    ProgramLevel selectedProgram,
    List<Education> education,
    List<ProfessionalExperience> experience,
    List<ExamPass> examPasses) {}
