package com.tol.tenderwork.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.tol.tenderwork.domain.Requirement;
import com.tol.tenderwork.domain.Tag;
import com.tol.tenderwork.domain.Task;
import com.tol.tenderwork.repository.RequirementRepository;
import com.tol.tenderwork.repository.search.RequirementSearchRepository;
import com.tol.tenderwork.service.DeleteService;
import com.tol.tenderwork.service.SaveService;
import com.tol.tenderwork.service.UpdateService;
import com.tol.tenderwork.web.rest.util.HeaderUtil;
import com.tol.tenderwork.web.rest.util.PaginationUtil;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Requirement.
 */
@RestController
@RequestMapping("/api")
public class RequirementResource {

    private final Logger log = LoggerFactory.getLogger(RequirementResource.class);

    @Inject
    private RequirementRepository requirementRepository;

    @Inject
    private RequirementSearchRepository requirementSearchRepository;

    @Autowired
    private UpdateService updateService;

    @Autowired
    private SaveService saveService;

    @Autowired
    private DeleteService deleteService;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private EntityManager taskEntityManager;

    /**
     * POST  /requirements -> Create a new requirement.
     */
    @RequestMapping(value = "/requirements",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @PreAuthorize("#requirement.getOwnerEstimate().getCreatedBy().getLogin().equals(authentication.name) OR hasRole('ROLE_ADMIN')")
    public ResponseEntity<Requirement> createRequirement(@Valid @RequestBody Requirement requirement) throws URISyntaxException {
        log.debug("REST request to save Requirement : {}", requirement);
        if (requirement.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("requirement", "idexists", "A new requirement cannot already have an ID")).body(null);
        }

        // Jos vaatimuksella on tageja, ne käsitellään
        if (!(requirement.getTags().isEmpty())) {
            for (Tag tag : requirement.getTags()) {
                tag.setName(tag.getName().toLowerCase());
                tag.addRequirement(requirement);
                saveService.saveTagToRepo(tag);
            }
        }

        Requirement result = updateService.updateRequirement(requirement);

        return ResponseEntity.created(new URI("/api/requirements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("requirement", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /requirements -> Updates an existing requirement.
     */
    @RequestMapping(value = "/requirements",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @PreAuthorize("#requirement.getOwner().getLogin().equals(authentication.name) OR hasRole('ROLE_ADMIN')")
    public ResponseEntity<Requirement> updateRequirement(@Valid @RequestBody Requirement requirement) throws URISyntaxException {
        log.debug("REST request to update Requirement : {}", requirement);
        if (requirement.getId() == null) {
            return createRequirement(requirement);
        }

        requirement = updateService.updateRequirementTags(requirement);
        Requirement result = updateService.updateRequirement(requirement);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("requirement", requirement.getId().toString()))
            .body(result);
    }

    /**
     * GET  /requirements -> get all the requirements.
     */
    @RequestMapping(value = "/requirements",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Requirement>> getAllRequirements(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Requirements");
        Page<Requirement> page = requirementRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/requirements");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /requirements/:id -> get the "id" requirement.
     */
    @RequestMapping(value = "/requirements/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Requirement> getRequirement(@PathVariable Long id) {
        log.debug("REST request to get Requirement : {}", id);
        Requirement requirement = requirementRepository.findOneWithEagerRelationships(id);
        return Optional.ofNullable(requirement)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /requirements/:id -> delete the "id" requirement.
     */
    @RequestMapping(value = "/requirements/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @PreAuthorize("@requirementRepository.findOne(#id).getOwner().getLogin().equals(authentication.name) OR hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteRequirement(@PathVariable Long id) {
        log.debug("REST request to delete Requirement : {}", id);
        deleteService.deleteRequirement(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("requirement", id.toString())).build();
    }

    /**
     * SEARCH  /_search/requirements/:query -> search for the requirement corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/requirements/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Requirement> searchRequirements(@PathVariable String query) {
        log.debug("REST request to search Requirements for query {}", query);
        return StreamSupport
            .stream(requirementSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

    /**
     * POST  /requirement/{id} -> Clone requirement.
     */
    @RequestMapping(value = "/requirements/{id}",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional
    @PreAuthorize("@requirementRepository.findOne(#id).getOwner().getLogin().equals(authentication.name) OR hasRole('ROLE_ADMIN')")
    public ResponseEntity<Requirement> cloneRequirement(@PathVariable Long id) throws URISyntaxException {
        log.debug("REST request to clone Requirement : {}", id);
        Requirement clone = entityManager.find(Requirement.class, id);
        if (clone.getId() == null) {
            return createRequirement(clone);
        }
        entityManager.detach(clone);
        clone.setId(null);
        clone.setName(clone.getName() + " - Kopio");
        Set<Task> newSet = new HashSet<>();
        if(!clone.getTags().isEmpty()) {
            for (Tag tag : clone.getTags()) {
                tag.addRequirement(clone);
                saveService.saveTagToRepo(tag);
            }
        }
        entityManager.persist(clone);
        entityManager.merge(clone);
        clone = saveService.saveRequirementToRepo(clone);
        Long cloneID = clone.getId();

        Hibernate.initialize(clone.getHasTaskss());
        log.error("TASKS: {}", clone.getHasTaskss());

        clone = entityManager.find(Requirement.class, cloneID);
        List<Task> tempSet = new ArrayList<>();
        if(!clone.getHasTaskss().isEmpty()) {
            for (Task task : clone.getHasTaskss()) {
                tempSet.add(task);
            }

            for (Task task : tempSet) {
                Task clonedTask = cloneTask(task, clone);
                newSet.add(clonedTask);
            }
            clone.setHasTaskss(newSet);
        }
        Requirement result = saveService.saveRequirementToRepo(clone);

        return ResponseEntity.created(new URI("/api/requirements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("requirement", result.getId().toString()))
            .body(result);
    }

    @Transactional
    public Task cloneTask(Task task, Requirement clone) {
        log.error("Request to CLONE TASK {}: ", task);
        Task taskClone = taskEntityManager.find(Task.class, task.getId());
        taskEntityManager.detach(taskClone);
        taskClone.setId(null);
        taskClone.setOwnerRequirement(clone);
        log.error("TASK: {}", taskClone);
        taskEntityManager.persist(taskClone);
        Hibernate.initialize(taskClone.getTags());
        if (!taskClone.getTags().isEmpty()) {
            for (Tag tag : taskClone.getTags()) {
                tag.addTask(taskClone);
                saveService.saveTagToRepo(tag);
            }
        }
        taskClone = updateService.updateTask(taskClone);
        saveService.saveTaskToRepo(taskClone);

        return taskClone;
    }
}
