package com.cfa.candidate.api.dto;

import com.cfa.candidate.domain.model.Candidate;
import com.cfa.candidate.domain.model.PageResult;
import io.micronaut.serde.annotation.Serdeable;
import java.util.List;

@Serdeable
public record CandidatePageResponse(
    List<CandidateResponse> content, int page, int size, long totalElements) {

  public static CandidatePageResponse fromDomain(PageResult<Candidate> pageResult) {
    return new CandidatePageResponse(
        pageResult.content().stream().map(CandidateResponse::fromDomain).toList(),
        pageResult.page(),
        pageResult.size(),
        pageResult.totalElements());
  }
}
