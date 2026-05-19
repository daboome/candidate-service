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
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Singleton
public class JpaCandidateRepository implements CandidateRepository {

  private final CandidateEntityRepository entityRepository;

  public JpaCandidateRepository(CandidateEntityRepository entityRepository) {
    this.entityRepository = entityRepository;
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
    Page<CandidateEntity> result =
        entityRepository.searchActive(
            status != null ? status.name() : null,
            program != null ? program.name() : null,
            Pageable.from(page, size));
    return new PageResult<>(
        result.getContent().stream().map(CandidateMapper::toDomain).toList(),
        page,
        size,
        result.getTotalSize());
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
