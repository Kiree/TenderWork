package com.tol.tenderwork.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.tol.tenderwork.domain.Testity;
import com.tol.tenderwork.repository.TestityRepository;
import com.tol.tenderwork.repository.search.TestitySearchRepository;
import com.tol.tenderwork.web.rest.util.HeaderUtil;
import com.tol.tenderwork.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing Testity.
 */
@RestController
@RequestMapping("/api")
public class TestityResource {

    private final Logger log = LoggerFactory.getLogger(TestityResource.class);
        
    @Inject
    private TestityRepository testityRepository;
    
    @Inject
    private TestitySearchRepository testitySearchRepository;
    
    /**
     * POST  /testitys -> Create a new testity.
     */
    @RequestMapping(value = "/testitys",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Testity> createTestity(@Valid @RequestBody Testity testity) throws URISyntaxException {
        log.debug("REST request to save Testity : {}", testity);
        if (testity.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("testity", "idexists", "A new testity cannot already have an ID")).body(null);
        }
        Testity result = testityRepository.save(testity);
        testitySearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/testitys/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("testity", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /testitys -> Updates an existing testity.
     */
    @RequestMapping(value = "/testitys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Testity> updateTestity(@Valid @RequestBody Testity testity) throws URISyntaxException {
        log.debug("REST request to update Testity : {}", testity);
        if (testity.getId() == null) {
            return createTestity(testity);
        }
        Testity result = testityRepository.save(testity);
        testitySearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("testity", testity.getId().toString()))
            .body(result);
    }

    /**
     * GET  /testitys -> get all the testitys.
     */
    @RequestMapping(value = "/testitys",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Testity>> getAllTestitys(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Testitys");
        Page<Testity> page = testityRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/testitys");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /testitys/:id -> get the "id" testity.
     */
    @RequestMapping(value = "/testitys/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Testity> getTestity(@PathVariable Long id) {
        log.debug("REST request to get Testity : {}", id);
        Testity testity = testityRepository.findOne(id);
        return Optional.ofNullable(testity)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /testitys/:id -> delete the "id" testity.
     */
    @RequestMapping(value = "/testitys/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTestity(@PathVariable Long id) {
        log.debug("REST request to delete Testity : {}", id);
        testityRepository.delete(id);
        testitySearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("testity", id.toString())).build();
    }

    /**
     * SEARCH  /_search/testitys/:query -> search for the testity corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/testitys/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Testity> searchTestitys(@PathVariable String query) {
        log.debug("REST request to search Testitys for query {}", query);
        return StreamSupport
            .stream(testitySearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
