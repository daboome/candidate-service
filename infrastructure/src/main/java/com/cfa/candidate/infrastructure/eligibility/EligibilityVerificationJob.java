package com.cfa.candidate.infrastructure.eligibility;

import com.cfa.candidate.domain.exception.CandidateNotFoundException;
import com.cfa.candidate.domain.model.Candidate;
import com.cfa.candidate.domain.model.EligibilityAuditEntry;
import com.cfa.candidate.domain.model.EligibilityStatus;
import com.cfa.candidate.domain.port.CandidateRepository;
import com.cfa.candidate.domain.port.EligibilityAuditRepository;
import com.cfa.candidate.domain.service.EligibilityEvaluator;
import com.cfa.candidate.domain.service.EligibilityResult;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Singleton;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class EligibilityVerificationJob {

  private static final Logger LOG = LoggerFactory.getLogger(EligibilityVerificationJob.class);

  private final CandidateRepository candidateRepository;
  private final EligibilityAuditRepository auditRepository;
  private final EligibilityEvaluator evaluator;
  private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

  public EligibilityVerificationJob(
      CandidateRepository candidateRepository, EligibilityAuditRepository auditRepository) {
    this.candidateRepository = candidateRepository;
    this.auditRepository = auditRepository;
    this.evaluator = new EligibilityEvaluator();
  }

  public void runAsync(UUID candidateId, String verifiedBy) {
    executor.execute(() -> verify(candidateId, verifiedBy));
  }

  private void verify(UUID candidateId, String verifiedBy) {
    try {
      Candidate candidate =
          candidateRepository
              .findById(candidateId)
              .orElseThrow(() -> new CandidateNotFoundException(candidateId));
      EligibilityResult result = evaluator.evaluate(candidate);
      candidateRepository.save(candidate.withStatus(result.status()));
      auditRepository.save(
          new EligibilityAuditEntry(
              UUID.randomUUID(),
              candidateId,
              verifiedBy,
              Instant.now(),
              result.status(),
              result.reason()));
      LOG.info(
          "Eligibility verification completed for candidate {}: {}", candidateId, result.status());
    } catch (Exception ex) {
      LOG.error("Eligibility verification failed for candidate {}", candidateId, ex);
      candidateRepository
          .findById(candidateId)
          .ifPresent(c -> candidateRepository.save(c.withStatus(EligibilityStatus.INELIGIBLE)));
    }
  }

  @PreDestroy
  void shutdown() {
    executor.shutdown();
  }
}
