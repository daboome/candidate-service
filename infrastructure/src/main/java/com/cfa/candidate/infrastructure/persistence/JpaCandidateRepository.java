package com.cfa.candidate.infrastructure.persistence;

import com.cfa.candidate.domain.exception.CandidateNotFoundException;
import com.cfa.candidate.domain.exception.DuplicateEmailException;
import com.cfa.candidate.domain.model.Candidate;
import com.cfa.candidate.domain.model.EligibilityStatus;
import com.cfa.candidate.domain.model.PageResult;
import com.cfa.candidate.domain.model.ProgramLevel;
import com.cfa.candidate.domain.port.CandidateRepository;
import com.cfa.candidate.infrastructure.persistence.entity.CandidateEntity;
import com.cfa.candidate.infrastructure.persistence.mapper.CandidateMapper;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Singleton
public class JpaCandidateRepository implements CandidateRepository {

  private final CandidateEntityRepository entityRepository;
  private final EntityManager entityManager;

  public JpaCandidateRepository(
      CandidateEntityRepository entityRepository, EntityManager entityManager) {
    this.entityRepository = entityRepository;
    this.entityManager = entityManager;
  }

  @Override
  @Transactional
  public Candidate save(Candidate candidate) {
    try {
      CandidateEntity entity =
          entityRepository
              .findById(candidate.id())
              .map(
                  existing -> {
                    CandidateMapper.updateEntity(existing, candidate);
                    return existing;
                  })
              .orElseGet(() -> CandidateMapper.toEntity(candidate));
      return CandidateMapper.toDomain(entityRepository.save(entity));
    } catch (RuntimeException ex) {
      if (isUniqueViolation(ex)) {
        throw new DuplicateEmailException(candidate.email());
      }
      throw ex;
    }
  }

  @Override
  @Transactional
  public Optional<Candidate> findById(UUID id) {
    return entityRepository.findByIdAndDeletedAtIsNull(id).map(CandidateMapper::toDomain);
  }

  @Override
  @Transactional
  public Optional<Candidate> findActiveByEmail(String email) {
    return entityRepository
        .findByEmailIgnoreCaseAndDeletedAtIsNull(email)
        .map(CandidateMapper::toDomain);
  }

  @Override
  public boolean existsActiveByEmail(String email) {
    return entityRepository.existsByEmailIgnoreCaseAndDeletedAtIsNull(email);
  }

  @Override
  @Transactional
  public PageResult<Candidate> search(
      EligibilityStatus status, ProgramLevel program, int page, int size) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();

    CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
    Root<CandidateEntity> countRoot = countQuery.from(CandidateEntity.class);
    countQuery.select(cb.count(countRoot)).where(predicates(cb, countRoot, status, program));
    long total = entityManager.createQuery(countQuery).getSingleResult();

    CriteriaQuery<CandidateEntity> dataQuery = cb.createQuery(CandidateEntity.class);
    Root<CandidateEntity> root = dataQuery.from(CandidateEntity.class);
    dataQuery
        .select(root)
        .where(predicates(cb, root, status, program))
        .orderBy(cb.desc(root.get("createdAt")));

    TypedQuery<CandidateEntity> typedQuery = entityManager.createQuery(dataQuery);
    typedQuery.setFirstResult(page * size);
    typedQuery.setMaxResults(size);
    List<Candidate> content =
        typedQuery.getResultList().stream().map(CandidateMapper::toDomain).toList();
    return new PageResult<>(content, page, size, total);
  }

  @Override
  @Transactional
  public void softDelete(UUID id) {
    CandidateEntity entity =
        entityRepository
            .findByIdAndDeletedAtIsNull(id)
            .orElseThrow(() -> new CandidateNotFoundException(id));
    entity.setDeletedAt(Instant.now());
    entity.setUpdatedAt(Instant.now());
    entityRepository.update(entity);
  }

  private static Predicate[] predicates(
      CriteriaBuilder cb,
      Root<CandidateEntity> root,
      EligibilityStatus status,
      ProgramLevel program) {
    List<Predicate> predicates = new ArrayList<>();
    predicates.add(cb.isNull(root.get("deletedAt")));
    if (status != null) {
      predicates.add(cb.equal(root.get("eligibilityStatus"), status.name()));
    }
    if (program != null) {
      predicates.add(cb.equal(root.get("selectedProgram"), program.name()));
    }
    return predicates.toArray(Predicate[]::new);
  }

  private static boolean isUniqueViolation(Throwable ex) {
    Throwable current = ex;
    while (current != null) {
      String message = current.getMessage();
      if (message != null
          && (message.contains("uk_candidates_email_active")
              || message.contains("duplicate key"))) {
        return true;
      }
      current = current.getCause();
    }
    return false;
  }
}
