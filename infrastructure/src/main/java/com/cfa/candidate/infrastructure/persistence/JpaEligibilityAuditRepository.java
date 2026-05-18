package com.cfa.candidate.infrastructure.persistence;

import com.cfa.candidate.domain.model.EligibilityAuditEntry;
import com.cfa.candidate.domain.model.EligibilityStatus;
import com.cfa.candidate.domain.port.EligibilityAuditRepository;
import com.cfa.candidate.infrastructure.persistence.entity.EligibilityAuditEntity;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Singleton
public class JpaEligibilityAuditRepository implements EligibilityAuditRepository {

  private final EligibilityAuditEntityRepository entityRepository;

  public JpaEligibilityAuditRepository(EligibilityAuditEntityRepository entityRepository) {
    this.entityRepository = entityRepository;
  }

  @Override
  @Transactional
  public EligibilityAuditEntry save(EligibilityAuditEntry entry) {
    EligibilityAuditEntity entity = new EligibilityAuditEntity();
    entity.setId(entry.id() != null ? entry.id() : UUID.randomUUID());
    entity.setCandidateId(entry.candidateId());
    entity.setVerifiedBy(entry.verifiedBy());
    entity.setVerifiedAt(entry.verifiedAt());
    entity.setDecision(entry.decision().name());
    entity.setReason(entry.reason());
    return toDomain(entityRepository.save(entity));
  }

  @Override
  public List<EligibilityAuditEntry> findByCandidateId(UUID candidateId) {
    return entityRepository.findByCandidateIdOrderByVerifiedAtDesc(candidateId).stream()
        .map(JpaEligibilityAuditRepository::toDomain)
        .toList();
  }

  private static EligibilityAuditEntry toDomain(EligibilityAuditEntity entity) {
    return new EligibilityAuditEntry(
        entity.getId(),
        entity.getCandidateId(),
        entity.getVerifiedBy(),
        entity.getVerifiedAt(),
        EligibilityStatus.valueOf(entity.getDecision()),
        entity.getReason());
  }
}
