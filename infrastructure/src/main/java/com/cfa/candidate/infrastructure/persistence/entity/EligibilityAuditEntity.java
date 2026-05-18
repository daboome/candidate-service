package com.cfa.candidate.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "eligibility_audits")
public class EligibilityAuditEntity {

  @Id private UUID id;

  @Column(name = "candidate_id", nullable = false)
  private UUID candidateId;

  @Column(name = "verified_by", nullable = false)
  private String verifiedBy;

  @Column(name = "verified_at", nullable = false)
  private Instant verifiedAt;

  @Column(nullable = false)
  private String decision;

  @Column(nullable = false)
  private String reason;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public UUID getCandidateId() {
    return candidateId;
  }

  public void setCandidateId(UUID candidateId) {
    this.candidateId = candidateId;
  }

  public String getVerifiedBy() {
    return verifiedBy;
  }

  public void setVerifiedBy(String verifiedBy) {
    this.verifiedBy = verifiedBy;
  }

  public Instant getVerifiedAt() {
    return verifiedAt;
  }

  public void setVerifiedAt(Instant verifiedAt) {
    this.verifiedAt = verifiedAt;
  }

  public String getDecision() {
    return decision;
  }

  public void setDecision(String decision) {
    this.decision = decision;
  }

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }
}
