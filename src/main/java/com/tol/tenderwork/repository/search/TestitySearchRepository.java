package com.tol.tenderwork.repository.search;

import com.tol.tenderwork.domain.Testity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Testity entity.
 */
public interface TestitySearchRepository extends ElasticsearchRepository<Testity, Long> {
}
