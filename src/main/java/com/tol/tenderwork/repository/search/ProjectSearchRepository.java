package com.tol.tenderwork.repository.search;

import com.tol.tenderwork.domain.Project;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Project entity.
 */
public interface ProjectSearchRepository extends ElasticsearchRepository<Project, Long> {

    Iterable<Project> findByNameOrClientOrTags_Name(String name, String client, String tag);
}
