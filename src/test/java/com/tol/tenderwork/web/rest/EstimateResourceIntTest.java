package com.tol.tenderwork.web.rest;

import com.tol.tenderwork.Application;
import com.tol.tenderwork.domain.Estimate;
import com.tol.tenderwork.repository.EstimateRepository;
import com.tol.tenderwork.repository.search.EstimateSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the EstimateResource REST controller.
 *
 * @see EstimateResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class EstimateResourceIntTest {

    private static final String DEFAULT_CREATOR = "AAAAA";
    private static final String UPDATED_CREATOR = "BBBBB";

    private static final Integer DEFAULT_WORK_DAYS = 1;
    private static final Integer UPDATED_WORK_DAYS = 2;

    private static final Integer DEFAULT_DURATION = 1;
    private static final Integer UPDATED_DURATION = 2;

    private static final BigDecimal DEFAULT_COST = new BigDecimal(1);
    private static final BigDecimal UPDATED_COST = new BigDecimal(2);

    private static final BigDecimal DEFAULT_MULTI_SPEC = new BigDecimal(1);
    private static final BigDecimal UPDATED_MULTI_SPEC = new BigDecimal(2);

    private static final BigDecimal DEFAULT_MULTI_IMP = new BigDecimal(1);
    private static final BigDecimal UPDATED_MULTI_IMP = new BigDecimal(2);

    private static final BigDecimal DEFAULT_MULTI_TEST = new BigDecimal(1);
    private static final BigDecimal UPDATED_MULTI_TEST = new BigDecimal(2);

    private static final BigDecimal DEFAULT_MULTI_SYN = new BigDecimal(1);
    private static final BigDecimal UPDATED_MULTI_SYN = new BigDecimal(2);

    private static final BigDecimal DEFAULT_OVERALL_COST = new BigDecimal(1);
    private static final BigDecimal UPDATED_OVERALL_COST = new BigDecimal(2);

    private static final BigDecimal DEFAULT_OVERALL_DURATION = new BigDecimal(1);
    private static final BigDecimal UPDATED_OVERALL_DURATION = new BigDecimal(2);

    private static final BigDecimal DEFAULT_OVERALL_RESOURCES = new BigDecimal(1);
    private static final BigDecimal UPDATED_OVERALL_RESOURCES = new BigDecimal(2);

    private static final BigDecimal DEFAULT_OVERALL_GAIN = new BigDecimal(1);
    private static final BigDecimal UPDATED_OVERALL_GAIN = new BigDecimal(2);

    @Inject
    private EstimateRepository estimateRepository;

    @Inject
    private EstimateSearchRepository estimateSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restEstimateMockMvc;

    private Estimate estimate;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EstimateResource estimateResource = new EstimateResource();
        ReflectionTestUtils.setField(estimateResource, "estimateSearchRepository", estimateSearchRepository);
        ReflectionTestUtils.setField(estimateResource, "estimateRepository", estimateRepository);
        this.restEstimateMockMvc = MockMvcBuilders.standaloneSetup(estimateResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        estimate = new Estimate();
        estimate.setCreator(DEFAULT_CREATOR);
        estimate.setWorkDays(DEFAULT_WORK_DAYS);
        estimate.setDuration(DEFAULT_DURATION);
        estimate.setCost(DEFAULT_COST);
        estimate.setMultiSpec(DEFAULT_MULTI_SPEC);
        estimate.setMultiImp(DEFAULT_MULTI_IMP);
        estimate.setMultiTest(DEFAULT_MULTI_TEST);
        estimate.setMultiSyn(DEFAULT_MULTI_SYN);
        estimate.setOverallCost(DEFAULT_OVERALL_COST);
        estimate.setOverallDuration(DEFAULT_OVERALL_DURATION);
        estimate.setOverallResources(DEFAULT_OVERALL_RESOURCES);
        estimate.setOverallGain(DEFAULT_OVERALL_GAIN);
    }

    @Test
    @Transactional
    public void createEstimate() throws Exception {
        int databaseSizeBeforeCreate = estimateRepository.findAll().size();

        // Create the Estimate

        restEstimateMockMvc.perform(post("/api/estimates")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(estimate)))
                .andExpect(status().isCreated());

        // Validate the Estimate in the database
        List<Estimate> estimates = estimateRepository.findAll();
        assertThat(estimates).hasSize(databaseSizeBeforeCreate + 1);
        Estimate testEstimate = estimates.get(estimates.size() - 1);
        assertThat(testEstimate.getCreator()).isEqualTo(DEFAULT_CREATOR);
        assertThat(testEstimate.getWorkDays()).isEqualTo(DEFAULT_WORK_DAYS);
        assertThat(testEstimate.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testEstimate.getCost()).isEqualTo(DEFAULT_COST);
        assertThat(testEstimate.getMultiSpec()).isEqualTo(DEFAULT_MULTI_SPEC);
        assertThat(testEstimate.getMultiImp()).isEqualTo(DEFAULT_MULTI_IMP);
        assertThat(testEstimate.getMultiTest()).isEqualTo(DEFAULT_MULTI_TEST);
        assertThat(testEstimate.getMultiSyn()).isEqualTo(DEFAULT_MULTI_SYN);
        assertThat(testEstimate.getOverallCost()).isEqualTo(DEFAULT_OVERALL_COST);
        assertThat(testEstimate.getOverallDuration()).isEqualTo(DEFAULT_OVERALL_DURATION);
        assertThat(testEstimate.getOverallResources()).isEqualTo(DEFAULT_OVERALL_RESOURCES);
        assertThat(testEstimate.getOverallGain()).isEqualTo(DEFAULT_OVERALL_GAIN);
    }

    @Test
    @Transactional
    public void getAllEstimates() throws Exception {
        // Initialize the database
        estimateRepository.saveAndFlush(estimate);

        // Get all the estimates
        restEstimateMockMvc.perform(get("/api/estimates?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(estimate.getId().intValue())))
                .andExpect(jsonPath("$.[*].creator").value(hasItem(DEFAULT_CREATOR.toString())))
                .andExpect(jsonPath("$.[*].workDays").value(hasItem(DEFAULT_WORK_DAYS)))
                .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)))
                .andExpect(jsonPath("$.[*].cost").value(hasItem(DEFAULT_COST.intValue())))
                .andExpect(jsonPath("$.[*].multiSpec").value(hasItem(DEFAULT_MULTI_SPEC.intValue())))
                .andExpect(jsonPath("$.[*].multiImp").value(hasItem(DEFAULT_MULTI_IMP.intValue())))
                .andExpect(jsonPath("$.[*].multiTest").value(hasItem(DEFAULT_MULTI_TEST.intValue())))
                .andExpect(jsonPath("$.[*].multiSyn").value(hasItem(DEFAULT_MULTI_SYN.intValue())))
                .andExpect(jsonPath("$.[*].overallCost").value(hasItem(DEFAULT_OVERALL_COST.intValue())))
                .andExpect(jsonPath("$.[*].overallDuration").value(hasItem(DEFAULT_OVERALL_DURATION.intValue())))
                .andExpect(jsonPath("$.[*].overallResources").value(hasItem(DEFAULT_OVERALL_RESOURCES.intValue())))
                .andExpect(jsonPath("$.[*].overallGain").value(hasItem(DEFAULT_OVERALL_GAIN.intValue())));
    }

    @Test
    @Transactional
    public void getEstimate() throws Exception {
        // Initialize the database
        estimateRepository.saveAndFlush(estimate);

        // Get the estimate
        restEstimateMockMvc.perform(get("/api/estimates/{id}", estimate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(estimate.getId().intValue()))
            .andExpect(jsonPath("$.creator").value(DEFAULT_CREATOR.toString()))
            .andExpect(jsonPath("$.workDays").value(DEFAULT_WORK_DAYS))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION))
            .andExpect(jsonPath("$.cost").value(DEFAULT_COST.intValue()))
            .andExpect(jsonPath("$.multiSpec").value(DEFAULT_MULTI_SPEC.intValue()))
            .andExpect(jsonPath("$.multiImp").value(DEFAULT_MULTI_IMP.intValue()))
            .andExpect(jsonPath("$.multiTest").value(DEFAULT_MULTI_TEST.intValue()))
            .andExpect(jsonPath("$.multiSyn").value(DEFAULT_MULTI_SYN.intValue()))
            .andExpect(jsonPath("$.overallCost").value(DEFAULT_OVERALL_COST.intValue()))
            .andExpect(jsonPath("$.overallDuration").value(DEFAULT_OVERALL_DURATION.intValue()))
            .andExpect(jsonPath("$.overallResources").value(DEFAULT_OVERALL_RESOURCES.intValue()))
            .andExpect(jsonPath("$.overallGain").value(DEFAULT_OVERALL_GAIN.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingEstimate() throws Exception {
        // Get the estimate
        restEstimateMockMvc.perform(get("/api/estimates/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEstimate() throws Exception {
        // Initialize the database
        estimateRepository.saveAndFlush(estimate);

		int databaseSizeBeforeUpdate = estimateRepository.findAll().size();

        // Update the estimate
        estimate.setCreator(UPDATED_CREATOR);
        estimate.setWorkDays(UPDATED_WORK_DAYS);
        estimate.setDuration(UPDATED_DURATION);
        estimate.setCost(UPDATED_COST);
        estimate.setMultiSpec(UPDATED_MULTI_SPEC);
        estimate.setMultiImp(UPDATED_MULTI_IMP);
        estimate.setMultiTest(UPDATED_MULTI_TEST);
        estimate.setMultiSyn(UPDATED_MULTI_SYN);
        estimate.setOverallCost(UPDATED_OVERALL_COST);
        estimate.setOverallDuration(UPDATED_OVERALL_DURATION);
        estimate.setOverallResources(UPDATED_OVERALL_RESOURCES);
        estimate.setOverallGain(UPDATED_OVERALL_GAIN);

        restEstimateMockMvc.perform(put("/api/estimates")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(estimate)))
                .andExpect(status().isOk());

        // Validate the Estimate in the database
        List<Estimate> estimates = estimateRepository.findAll();
        assertThat(estimates).hasSize(databaseSizeBeforeUpdate);
        Estimate testEstimate = estimates.get(estimates.size() - 1);
        assertThat(testEstimate.getCreator()).isEqualTo(UPDATED_CREATOR);
        assertThat(testEstimate.getWorkDays()).isEqualTo(UPDATED_WORK_DAYS);
        assertThat(testEstimate.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testEstimate.getCost()).isEqualTo(UPDATED_COST);
        assertThat(testEstimate.getMultiSpec()).isEqualTo(UPDATED_MULTI_SPEC);
        assertThat(testEstimate.getMultiImp()).isEqualTo(UPDATED_MULTI_IMP);
        assertThat(testEstimate.getMultiTest()).isEqualTo(UPDATED_MULTI_TEST);
        assertThat(testEstimate.getMultiSyn()).isEqualTo(UPDATED_MULTI_SYN);
        assertThat(testEstimate.getOverallCost()).isEqualTo(UPDATED_OVERALL_COST);
        assertThat(testEstimate.getOverallDuration()).isEqualTo(UPDATED_OVERALL_DURATION);
        assertThat(testEstimate.getOverallResources()).isEqualTo(UPDATED_OVERALL_RESOURCES);
        assertThat(testEstimate.getOverallGain()).isEqualTo(UPDATED_OVERALL_GAIN);
    }

    @Test
    @Transactional
    public void deleteEstimate() throws Exception {
        // Initialize the database
        estimateRepository.saveAndFlush(estimate);

		int databaseSizeBeforeDelete = estimateRepository.findAll().size();

        // Get the estimate
        restEstimateMockMvc.perform(delete("/api/estimates/{id}", estimate.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Estimate> estimates = estimateRepository.findAll();
        assertThat(estimates).hasSize(databaseSizeBeforeDelete - 1);
    }
}
