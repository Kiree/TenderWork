package com.tol.tenderwork.repository.search;

import com.tol.tenderwork.domain.Requirement;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Requirement entity.
 */
public interface RequirementSearchRepository extends ElasticsearchRepository<Requirement, Long> {
}
