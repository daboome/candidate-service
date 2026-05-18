package com.cfa.candidate.infrastructure.persistence.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "candidates")
public class CandidateEntity {

  @Id private UUID id;

  @Column(nullable = false)
  private String email;

  @Column(name = "first_name", nullable = false)
  private String firstName;

  @Column(name = "last_name", nullable = false)
  private String lastName;

  @Column(name = "selected_program", nullable = false)
  private String selectedProgram;

  @Column(name = "eligibility_status", nullable = false)
  private String eligibilityStatus;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

  @Column(name = "deleted_at")
  private Instant deletedAt;

  @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<EducationEntity> education = new ArrayList<>();

  @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ProfessionalExperienceEntity> experience = new ArrayList<>();

  @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ExamPassEntity> examPasses = new ArrayList<>();

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getSelectedProgram() {
    return selectedProgram;
  }

  public void setSelectedProgram(String selectedProgram) {
    this.selectedProgram = selectedProgram;
  }

  public String getEligibilityStatus() {
    return eligibilityStatus;
  }

  public void setEligibilityStatus(String eligibilityStatus) {
    this.eligibilityStatus = eligibilityStatus;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Instant updatedAt) {
    this.updatedAt = updatedAt;
  }

  public Instant getDeletedAt() {
    return deletedAt;
  }

  public void setDeletedAt(Instant deletedAt) {
    this.deletedAt = deletedAt;
  }

  public List<EducationEntity> getEducation() {
    return education;
  }

  public void setEducation(List<EducationEntity> education) {
    this.education = education;
  }

  public List<ProfessionalExperienceEntity> getExperience() {
    return experience;
  }

  public void setExperience(List<ProfessionalExperienceEntity> experience) {
    this.experience = experience;
  }

  public List<ExamPassEntity> getExamPasses() {
    return examPasses;
  }

  public void setExamPasses(List<ExamPassEntity> examPasses) {
    this.examPasses = examPasses;
  }
}
