package com.cfa.candidate.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "education")
public class EducationEntity {

  @Id private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "candidate_id", nullable = false)
  private CandidateEntity candidate;

  @Column(name = "degree_level", nullable = false)
  private String degreeLevel;

  @Column(nullable = false)
  private String institution;

  @Column(name = "graduation_year")
  private Integer graduationYear;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public CandidateEntity getCandidate() {
    return candidate;
  }

  public void setCandidate(CandidateEntity candidate) {
    this.candidate = candidate;
  }

  public String getDegreeLevel() {
    return degreeLevel;
  }

  public void setDegreeLevel(String degreeLevel) {
    this.degreeLevel = degreeLevel;
  }

  public String getInstitution() {
    return institution;
  }

  public void setInstitution(String institution) {
    this.institution = institution;
  }

  public Integer getGraduationYear() {
    return graduationYear;
  }

  public void setGraduationYear(Integer graduationYear) {
    this.graduationYear = graduationYear;
  }
}
