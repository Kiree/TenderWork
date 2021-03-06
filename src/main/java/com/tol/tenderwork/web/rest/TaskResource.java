package com.tol.tenderwork.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.tol.tenderwork.domain.Tag;
import com.tol.tenderwork.domain.Task;
import com.tol.tenderwork.repository.TaskRepository;
import com.tol.tenderwork.repository.search.TaskSearchRepository;
import com.tol.tenderwork.service.UpdateService;
import com.tol.tenderwork.service.DeleteService;
import com.tol.tenderwork.service.SaveService;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
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
 * REST controller for managing Task.
 */
@RestController
@RequestMapping("/api")
public class TaskResource {

    private final Logger log = LoggerFactory.getLogger(TaskResource.class);

    @Inject
    private TaskRepository taskRepository;

    @Inject
    private TaskSearchRepository taskSearchRepository;

    @Autowired
    private UpdateService updateService;

    @Autowired
    private SaveService saveService;

    @Autowired
    private DeleteService deleteService;

    /**
     * POST  /tasks -> Create a new task.
     */
    @RequestMapping(value = "/tasks",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional
    @PreAuthorize("#task.getOwnerRequirement().getOwner().getLogin().equals(authentication.name) OR hasRole('ROLE_ADMIN')")
    public ResponseEntity<Task> createTask(@Valid @RequestBody Task task) throws URISyntaxException {
        log.debug("REST request to save Task : {}", task);
        if (task.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("task", "idexists", "A new task cannot already have an ID")).body(null);
        }

        // Jos taskilla on tageja, ne käsitellään
        if (!(task.getTags().isEmpty())) {
            for (Tag tag : task.getTags()) {
                tag.setName(tag.getName().toLowerCase());
                tag.addTask(task);
                saveService.saveTagToRepo(tag);
            }
        }

        Task result = updateService.updateTask(task);

        return ResponseEntity.created(new URI("/api/tasks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("task", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tasks -> Updates an existing task.
     */
    @RequestMapping(value = "/tasks",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional
    @PreAuthorize("#task.getOwnedBy().getLogin().equals(authentication.name) OR hasRole('ROLE_ADMIN')")
    public ResponseEntity<Task> updateTask(@Valid @RequestBody Task task) throws URISyntaxException {
        log.debug("REST request to update Task : {}", task);
        if (task.getId() == null) {
            return createTask(task);
        }

        task = updateService.updateTaskTags(task);
        Task result = updateService.updateTask(task);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("task", task.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tasks -> get all the tasks.
     */
    @RequestMapping(value = "/tasks",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Task>> getAllTasks(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Tasks");
        Page<Task> page = taskRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tasks");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /tasks/:id -> get the "id" task.
     */
    @RequestMapping(value = "/tasks/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Task> getTask(@PathVariable Long id) {
        log.debug("REST request to get Task : {}", id);
        Task task = taskRepository.findOneWithEagerRelationships(id);
        return Optional.ofNullable(task)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /tasks/:id -> delete the "id" task.
     */
    @RequestMapping(value = "/tasks/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional
    @PreAuthorize("@taskRepository.findOne(#id).getOwnedBy().getLogin().equals(authentication.name) OR hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        log.debug("REST request to delete Task : {}", id);

        //Delete the task
        deleteService.deleteTask(id);
        log.debug("Deleted task {}", id);

        //Force the Owner Requirement to update its values
        log.error("Forcing update");
        //updateController.updateAllTasks(task.getOwnerRequirement().getOwnerEstimate());

        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("task", id.toString())).build();
    }

    /**
     * SEARCH  /_search/tasks/:query -> search for the task corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/tasks/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Task> searchTasks(@PathVariable String query) {
        log.debug("REST request to search Tasks for query {}", query);
        return StreamSupport
            .stream(taskSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
