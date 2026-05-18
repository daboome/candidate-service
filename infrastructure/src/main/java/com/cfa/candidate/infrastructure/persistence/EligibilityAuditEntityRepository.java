package com.cfa.candidate.infrastructure.persistence;

import com.cfa.candidate.infrastructure.persistence.entity.EligibilityAuditEntity;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

@Repository
public interface EligibilityAuditEntityRepository
    extends JpaRepository<EligibilityAuditEntity, UUID> {

  List<EligibilityAuditEntity> findByCandidateIdOrderByVerifiedAtDesc(UUID candidateId);
}
