package com.cfa.candidate.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "exam_passes")
public class ExamPassEntity {

  @Id private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "candidate_id", nullable = false)
  private CandidateEntity candidate;

  @Column(nullable = false)
  private String level;

  @Column(name = "passed_at", nullable = false)
  private LocalDate passedAt;

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

  public String getLevel() {
    return level;
  }

  public void setLevel(String level) {
    this.level = level;
  }

  public LocalDate getPassedAt() {
    return passedAt;
  }

  public void setPassedAt(LocalDate passedAt) {
    this.passedAt = passedAt;
  }
}
