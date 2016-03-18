package com.tol.tenderwork.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.tol.tenderwork.domain.Estimate;
import com.tol.tenderwork.repository.EstimateRepository;
import com.tol.tenderwork.repository.search.EstimateSearchRepository;
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
 * REST controller for managing Estimate.
 */
@RestController
@RequestMapping("/api")
public class EstimateResource {

    private final Logger log = LoggerFactory.getLogger(EstimateResource.class);

    @Inject
    private EstimateRepository estimateRepository;

    @Inject
    private EstimateSearchRepository estimateSearchRepository;

    @Autowired
    private UpdateService updateService;

    @Autowired
    private SaveService saveService;

    @Autowired
    private DeleteService deleteService;

    /**
     * POST  /estimates -> Create a new estimate.
     */
    @RequestMapping(value = "/estimates",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional
    public ResponseEntity<Estimate> createEstimate(@Valid @RequestBody Estimate estimate) throws URISyntaxException {
        log.debug("REST request to save Estimate : {}", estimate);
        if (estimate.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("estimate", "idexists", "A new estimate cannot already have an ID")).body(null);
        }
        Estimate result = saveService.saveEstimateToRepo(estimate);
        updateService.updateProject(estimate.getOwnerProject(), estimate.getCreatedBy());

        return ResponseEntity.created(new URI("/api/estimates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("estimate", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /estimates -> Updates an existing estimate.
     */
    @RequestMapping(value = "/estimates",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional
    public ResponseEntity<Estimate> updateEstimate(@Valid @RequestBody Estimate estimate) throws URISyntaxException {
        log.debug("REST request to update Estimate : {}", estimate);
        if (estimate.getId() == null) {
            return createEstimate(estimate);
        }

            estimate = updateService.updateEstimateCall(estimate);
            updateService.updateProject(estimate.getOwnerProject(), estimate.getCreatedBy());


        // Save edited estimate
        Estimate result = estimateRepository.save(estimate);
        estimateSearchRepository.save(result);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("estimate", estimate.getId().toString()))
            .body(result);
    }

    /**
     * GET  /estimates -> get all the estimates.
     */
    @RequestMapping(value = "/estimates",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Estimate>> getAllEstimates(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Estimates");
        Page<Estimate> page = estimateRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/estimates");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /estimates/:id -> get the "id" estimate.
     */
    @RequestMapping(value = "/estimates/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Estimate> getEstimate(@PathVariable Long id) {
        log.debug("REST request to get Estimate : {}", id);
        Estimate estimate = estimateRepository.findOne(id);
        return Optional.ofNullable(estimate)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /estimates/:id -> delete the "id" estimate.
     */
    @RequestMapping(value = "/estimates/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteEstimate(@PathVariable Long id) {
        log.debug("REST request to delete Estimate : {}", id);

        deleteService.deleteEstimate(id);

        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("estimate", id.toString())).build();
    }

    /**
     * SEARCH  /_search/estimates/:query -> search for the estimate corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/estimates/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Estimate> searchEstimates(@PathVariable String query) {
        log.debug("REST request to search Estimates for query {}", query);
        return StreamSupport
            .stream(estimateSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
