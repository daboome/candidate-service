package com.cfa.candidate.infrastructure.persistence;

import com.cfa.candidate.infrastructure.persistence.entity.CandidateEntity;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CandidateEntityRepository extends JpaRepository<CandidateEntity, UUID> {

  Optional<CandidateEntity> findByIdAndDeletedAtIsNull(UUID id);

  Optional<CandidateEntity> findByEmailIgnoreCaseAndDeletedAtIsNull(String email);

  boolean existsByEmailIgnoreCaseAndDeletedAtIsNull(String email);
}
