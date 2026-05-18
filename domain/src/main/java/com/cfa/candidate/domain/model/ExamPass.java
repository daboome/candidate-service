package com.cfa.candidate.domain.model;

import java.time.LocalDate;

public record ExamPass(ProgramLevel level, LocalDate passedAt) {}
