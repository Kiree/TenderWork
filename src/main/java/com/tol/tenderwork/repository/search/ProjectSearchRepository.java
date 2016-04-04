package com.tol.tenderwork.repository.search;

import com.tol.tenderwork.domain.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * Spring Data ElasticSearch repository for the Project entity.
 */
public interface ProjectSearchRepository extends ElasticsearchRepository<Project, Long> {

    //@Query("{\"bool\": {\"should\": [{\"match\": {\"project.name\": \"?0\"}}, {\"match\": {\"project.client\"}}]}}")
    Iterable<Project> findByNameOrClientOrTags(String name, String client, String tag);

}
