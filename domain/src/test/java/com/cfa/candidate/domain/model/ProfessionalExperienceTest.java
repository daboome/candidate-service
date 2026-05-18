package com.cfa.candidate.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class ProfessionalExperienceTest {

  @Test
  void years_calculatesInclusiveDuration() {
    var experience =
        new ProfessionalExperience(
            "Corp", "Role", LocalDate.of(2020, 6, 1), LocalDate.of(2024, 5, 31));

    assertEquals(3, experience.years());
  }

  @Test
  void years_usesTodayWhenEndDateMissing() {
    var experience =
        new ProfessionalExperience("Corp", "Role", LocalDate.now().minusYears(5), null);

    assertEquals(5, experience.years());
  }
}
