package com.cfa.candidate.domain.port;

import com.cfa.candidate.domain.model.EligibilityAuditEntry;
import java.util.List;
import java.util.UUID;

public interface EligibilityAuditRepository {

  EligibilityAuditEntry save(EligibilityAuditEntry entry);

  List<EligibilityAuditEntry> findByCandidateId(UUID candidateId);
}
