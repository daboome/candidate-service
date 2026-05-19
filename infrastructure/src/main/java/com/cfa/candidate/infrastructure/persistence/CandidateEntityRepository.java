package com.cfa.candidate.infrastructure.persistence;

import com.cfa.candidate.infrastructure.persistence.entity.CandidateEntity;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CandidateEntityRepository extends JpaRepository<CandidateEntity, UUID> {

  Optional<CandidateEntity> findByIdAndDeletedAtIsNull(UUID id);

  Optional<CandidateEntity> findByEmailIgnoreCaseAndDeletedAtIsNull(String email);

  boolean existsByEmailIgnoreCaseAndDeletedAtIsNull(String email);

  @Query(
      value =
          """
          SELECT c FROM CandidateEntity c
          WHERE c.deletedAt IS NULL
            AND (:status IS NULL OR c.eligibilityStatus = :status)
            AND (:program IS NULL OR c.selectedProgram = :program)
          ORDER BY c.createdAt DESC
          """,
      countQuery =
          """
          SELECT COUNT(c) FROM CandidateEntity c
          WHERE c.deletedAt IS NULL
            AND (:status IS NULL OR c.eligibilityStatus = :status)
            AND (:program IS NULL OR c.selectedProgram = :program)
          """)
  Page<CandidateEntity> searchActive(
      @Nullable String status, @Nullable String program, Pageable pageable);
}
