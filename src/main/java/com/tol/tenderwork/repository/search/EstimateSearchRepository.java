package com.tol.tenderwork.repository.search;

import com.tol.tenderwork.domain.Estimate;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Estimate entity.
 */
public interface EstimateSearchRepository extends ElasticsearchRepository<Estimate, Long> {
}
