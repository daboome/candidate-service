package com.cfa.candidate.domain.model;

import java.time.LocalDate;

public record ProfessionalExperience(
    String employer, String role, LocalDate startDate, LocalDate endDate) {

  public int years() {
    LocalDate effectiveEnd = endDate != null ? endDate : LocalDate.now();
    int years = effectiveEnd.getYear() - startDate.getYear();
    if (effectiveEnd.getMonthValue() < startDate.getMonthValue()
        || (effectiveEnd.getMonthValue() == startDate.getMonthValue()
            && effectiveEnd.getDayOfMonth() < startDate.getDayOfMonth())) {
      years--;
    }
    return Math.max(years, 0);
  }
}
