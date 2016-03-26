package com.tol.tenderwork.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.tol.tenderwork.domain.Project;
import com.tol.tenderwork.domain.Tag;
import com.tol.tenderwork.repository.ProjectRepository;
import com.tol.tenderwork.repository.search.ProjectSearchRepository;
import com.tol.tenderwork.service.DeleteService;
import com.tol.tenderwork.service.SaveService;
import com.tol.tenderwork.service.UpdateService;
import com.tol.tenderwork.web.rest.util.HeaderUtil;
import com.tol.tenderwork.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Project.
 */
@RestController
@RequestMapping("/api")
public class ProjectResource {

    private final Logger log = LoggerFactory.getLogger(ProjectResource.class);

    @Inject
    private ProjectRepository projectRepository;

    @Inject
    private ProjectSearchRepository projectSearchRepository;

    @Autowired
    private SaveService saveService;

    @Autowired
    private DeleteService deleteService;

    @Autowired
    private UpdateService updateService;

    /**
     * POST  /projects -> Create a new project.
     */
    @RequestMapping(value = "/projects",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Project> createProject(@Valid @RequestBody Project project) throws URISyntaxException {
        log.debug("REST request to save Project : {}", project);

        if (!(project.getTags().isEmpty())) {
            for (Tag tag : project.getTags()) {
                //tag.addProject(project);
                saveService.saveTagToRepo(tag);
            }

            for (Tag tag : project.getTags()) {
                tag.setName(tag.getName().toLowerCase());
                //tag.addProject(project);
                saveService.saveTagToRepo(tag);

            }
        }
            if (project.getId() != null) {
                return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("project", "idexists", "A new project cannot already have an ID")).body(null);
            }
            Project result = saveService.saveProjectToRepo(project);

            return ResponseEntity.created(new URI("/api/projects/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("project", result.getId().toString()))
                .body(result);
        }


    /**
     * PUT  /projects -> Updates an existing project.
     */

    @RequestMapping(value = "/projects",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Project> updateProject(@Valid @RequestBody Project project) throws URISyntaxException {
        log.debug("REST request to update Project : {}", project);
        if (project.getId() == null) {
            return createProject(project);
        }
        project = updateService.updateProjectTags(project);
        Project result = saveService.saveProjectToRepo(project);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("project", project.getId().toString()))
            .body(result);
    }

    /**
     * GET  /projects -> get all the projects.
     */
    @RequestMapping(value = "/projects",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Project>> getAllProjects(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Projects");
        Page<Project> page = projectRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/projects");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /projects/:id -> get the "id" project.
     */
    @RequestMapping(value = "/projects/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Project> getProject(@PathVariable Long id) {
        log.debug("REST request to get Project : {}", id);
        Project project = projectRepository.findOne(id);
        return Optional.ofNullable(project)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /projects/:id -> delete the "id" project.
     */
    @RequestMapping(value = "/projects/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        log.debug("REST request to delete Project : {}", id);
        deleteService.deleteProject(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("project", id.toString())).build();
    }

    /**
     * SEARCH  /_search/projects/:query -> search for the project corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/projects/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Project> searchProjects(@PathVariable String query) {
        log.debug("REST request to search Projects for query {}", query);
        return StreamSupport
            .stream(projectSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}

