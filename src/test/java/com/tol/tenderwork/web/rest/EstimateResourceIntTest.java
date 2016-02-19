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


    private static final Integer DEFAULT_WORKDAYS_IN_MONTH = 1;
    private static final Integer UPDATED_WORKDAYS_IN_MONTH = 2;

    private static final Integer DEFAULT_DESIRED_PROJECT_DURATION = 1;
    private static final Integer UPDATED_DESIRED_PROJECT_DURATION = 2;

    private static final Long DEFAULT_DAILY_PRICE = 1L;
    private static final Long UPDATED_DAILY_PRICE = 2L;

    private static final Float DEFAULT_SPECIFICATION_FACTOR = 0F;
    private static final Float UPDATED_SPECIFICATION_FACTOR = 1F;

    private static final Float DEFAULT_TESTING_FACTOR = 0F;
    private static final Float UPDATED_TESTING_FACTOR = 1F;

    private static final Float DEFAULT_IMPLEMENTATION_FACTOR = 0F;
    private static final Float UPDATED_IMPLEMENTATION_FACTOR = 1F;

    private static final Float DEFAULT_SYNERGY_BENEFIT = 0F;
    private static final Float UPDATED_SYNERGY_BENEFIT = 1F;

    private static final Long DEFAULT_TOTAL_PRICE = 1L;
    private static final Long UPDATED_TOTAL_PRICE = 2L;

    private static final Float DEFAULT_TOTAL_DURATION = 0F;
    private static final Float UPDATED_TOTAL_DURATION = 1F;

    private static final Float DEFAULT_RESOURCING = 0F;
    private static final Float UPDATED_RESOURCING = 1F;

    private static final Float DEFAULT_TOTAL_SYNERGY_BENEFIT = 0F;
    private static final Float UPDATED_TOTAL_SYNERGY_BENEFIT = 1F;

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
        estimate.setWorkdaysInMonth(DEFAULT_WORKDAYS_IN_MONTH);
        estimate.setDesiredProjectDuration(DEFAULT_DESIRED_PROJECT_DURATION);
        estimate.setDailyPrice(DEFAULT_DAILY_PRICE);
        estimate.setSpecificationFactor(DEFAULT_SPECIFICATION_FACTOR);
        estimate.setTestingFactor(DEFAULT_TESTING_FACTOR);
        estimate.setImplementationFactor(DEFAULT_IMPLEMENTATION_FACTOR);
        estimate.setSynergyBenefit(DEFAULT_SYNERGY_BENEFIT);
        estimate.setTotalPrice(DEFAULT_TOTAL_PRICE);
        estimate.setTotalDuration(DEFAULT_TOTAL_DURATION);
        estimate.setResourcing(DEFAULT_RESOURCING);
        estimate.setTotalSynergyBenefit(DEFAULT_TOTAL_SYNERGY_BENEFIT);
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
        assertThat(testEstimate.getWorkdaysInMonth()).isEqualTo(DEFAULT_WORKDAYS_IN_MONTH);
        assertThat(testEstimate.getDesiredProjectDuration()).isEqualTo(DEFAULT_DESIRED_PROJECT_DURATION);
        assertThat(testEstimate.getDailyPrice()).isEqualTo(DEFAULT_DAILY_PRICE);
        assertThat(testEstimate.getSpecificationFactor()).isEqualTo(DEFAULT_SPECIFICATION_FACTOR);
        assertThat(testEstimate.getTestingFactor()).isEqualTo(DEFAULT_TESTING_FACTOR);
        assertThat(testEstimate.getImplementationFactor()).isEqualTo(DEFAULT_IMPLEMENTATION_FACTOR);
        assertThat(testEstimate.getSynergyBenefit()).isEqualTo(DEFAULT_SYNERGY_BENEFIT);
        assertThat(testEstimate.getTotalPrice()).isEqualTo(DEFAULT_TOTAL_PRICE);
        assertThat(testEstimate.getTotalDuration()).isEqualTo(DEFAULT_TOTAL_DURATION);
        assertThat(testEstimate.getResourcing()).isEqualTo(DEFAULT_RESOURCING);
        assertThat(testEstimate.getTotalSynergyBenefit()).isEqualTo(DEFAULT_TOTAL_SYNERGY_BENEFIT);
    }

    @Test
    @Transactional
    public void checkWorkdaysInMonthIsRequired() throws Exception {
        int databaseSizeBeforeTest = estimateRepository.findAll().size();
        // set the field null
        estimate.setWorkdaysInMonth(null);

        // Create the Estimate, which fails.

        restEstimateMockMvc.perform(post("/api/estimates")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(estimate)))
                .andExpect(status().isBadRequest());

        List<Estimate> estimates = estimateRepository.findAll();
        assertThat(estimates).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDesiredProjectDurationIsRequired() throws Exception {
        int databaseSizeBeforeTest = estimateRepository.findAll().size();
        // set the field null
        estimate.setDesiredProjectDuration(null);

        // Create the Estimate, which fails.

        restEstimateMockMvc.perform(post("/api/estimates")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(estimate)))
                .andExpect(status().isBadRequest());

        List<Estimate> estimates = estimateRepository.findAll();
        assertThat(estimates).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDailyPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = estimateRepository.findAll().size();
        // set the field null
        estimate.setDailyPrice(null);

        // Create the Estimate, which fails.

        restEstimateMockMvc.perform(post("/api/estimates")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(estimate)))
                .andExpect(status().isBadRequest());

        List<Estimate> estimates = estimateRepository.findAll();
        assertThat(estimates).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSpecificationFactorIsRequired() throws Exception {
        int databaseSizeBeforeTest = estimateRepository.findAll().size();
        // set the field null
        estimate.setSpecificationFactor(null);

        // Create the Estimate, which fails.

        restEstimateMockMvc.perform(post("/api/estimates")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(estimate)))
                .andExpect(status().isBadRequest());

        List<Estimate> estimates = estimateRepository.findAll();
        assertThat(estimates).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTestingFactorIsRequired() throws Exception {
        int databaseSizeBeforeTest = estimateRepository.findAll().size();
        // set the field null
        estimate.setTestingFactor(null);

        // Create the Estimate, which fails.

        restEstimateMockMvc.perform(post("/api/estimates")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(estimate)))
                .andExpect(status().isBadRequest());

        List<Estimate> estimates = estimateRepository.findAll();
        assertThat(estimates).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkImplementationFactorIsRequired() throws Exception {
        int databaseSizeBeforeTest = estimateRepository.findAll().size();
        // set the field null
        estimate.setImplementationFactor(null);

        // Create the Estimate, which fails.

        restEstimateMockMvc.perform(post("/api/estimates")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(estimate)))
                .andExpect(status().isBadRequest());

        List<Estimate> estimates = estimateRepository.findAll();
        assertThat(estimates).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSynergyBenefitIsRequired() throws Exception {
        int databaseSizeBeforeTest = estimateRepository.findAll().size();
        // set the field null
        estimate.setSynergyBenefit(null);

        // Create the Estimate, which fails.

        restEstimateMockMvc.perform(post("/api/estimates")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(estimate)))
                .andExpect(status().isBadRequest());

        List<Estimate> estimates = estimateRepository.findAll();
        assertThat(estimates).hasSize(databaseSizeBeforeTest);
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
                .andExpect(jsonPath("$.[*].workdaysInMonth").value(hasItem(DEFAULT_WORKDAYS_IN_MONTH)))
                .andExpect(jsonPath("$.[*].desiredProjectDuration").value(hasItem(DEFAULT_DESIRED_PROJECT_DURATION)))
                .andExpect(jsonPath("$.[*].dailyPrice").value(hasItem(DEFAULT_DAILY_PRICE.intValue())))
                .andExpect(jsonPath("$.[*].specificationFactor").value(hasItem(DEFAULT_SPECIFICATION_FACTOR.doubleValue())))
                .andExpect(jsonPath("$.[*].testingFactor").value(hasItem(DEFAULT_TESTING_FACTOR.doubleValue())))
                .andExpect(jsonPath("$.[*].implementationFactor").value(hasItem(DEFAULT_IMPLEMENTATION_FACTOR.doubleValue())))
                .andExpect(jsonPath("$.[*].synergyBenefit").value(hasItem(DEFAULT_SYNERGY_BENEFIT.doubleValue())))
                .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(DEFAULT_TOTAL_PRICE.intValue())))
                .andExpect(jsonPath("$.[*].totalDuration").value(hasItem(DEFAULT_TOTAL_DURATION)))
                .andExpect(jsonPath("$.[*].resourcing").value(hasItem(DEFAULT_RESOURCING)))
                .andExpect(jsonPath("$.[*].totalSynergyBenefit").value(hasItem(DEFAULT_TOTAL_SYNERGY_BENEFIT)));
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
            .andExpect(jsonPath("$.workdaysInMonth").value(DEFAULT_WORKDAYS_IN_MONTH))
            .andExpect(jsonPath("$.desiredProjectDuration").value(DEFAULT_DESIRED_PROJECT_DURATION))
            .andExpect(jsonPath("$.dailyPrice").value(DEFAULT_DAILY_PRICE.intValue()))
            .andExpect(jsonPath("$.specificationFactor").value(DEFAULT_SPECIFICATION_FACTOR.doubleValue()))
            .andExpect(jsonPath("$.testingFactor").value(DEFAULT_TESTING_FACTOR.doubleValue()))
            .andExpect(jsonPath("$.implementationFactor").value(DEFAULT_IMPLEMENTATION_FACTOR.doubleValue()))
            .andExpect(jsonPath("$.synergyBenefit").value(DEFAULT_SYNERGY_BENEFIT.doubleValue()))
            .andExpect(jsonPath("$.totalPrice").value(DEFAULT_TOTAL_PRICE.intValue()))
            .andExpect(jsonPath("$.totalDuration").value(DEFAULT_TOTAL_DURATION))
            .andExpect(jsonPath("$.resourcing").value(DEFAULT_RESOURCING))
            .andExpect(jsonPath("$.totalSynergyBenefit").value(DEFAULT_TOTAL_SYNERGY_BENEFIT));
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
        estimate.setWorkdaysInMonth(UPDATED_WORKDAYS_IN_MONTH);
        estimate.setDesiredProjectDuration(UPDATED_DESIRED_PROJECT_DURATION);
        estimate.setDailyPrice(UPDATED_DAILY_PRICE);
        estimate.setSpecificationFactor(UPDATED_SPECIFICATION_FACTOR);
        estimate.setTestingFactor(UPDATED_TESTING_FACTOR);
        estimate.setImplementationFactor(UPDATED_IMPLEMENTATION_FACTOR);
        estimate.setSynergyBenefit(UPDATED_SYNERGY_BENEFIT);
        estimate.setTotalPrice(UPDATED_TOTAL_PRICE);
        estimate.setTotalDuration(UPDATED_TOTAL_DURATION);
        estimate.setResourcing(UPDATED_RESOURCING);
        estimate.setTotalSynergyBenefit(UPDATED_TOTAL_SYNERGY_BENEFIT);

        restEstimateMockMvc.perform(put("/api/estimates")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(estimate)))
                .andExpect(status().isOk());

        // Validate the Estimate in the database
        List<Estimate> estimates = estimateRepository.findAll();
        assertThat(estimates).hasSize(databaseSizeBeforeUpdate);
        Estimate testEstimate = estimates.get(estimates.size() - 1);
        assertThat(testEstimate.getWorkdaysInMonth()).isEqualTo(UPDATED_WORKDAYS_IN_MONTH);
        assertThat(testEstimate.getDesiredProjectDuration()).isEqualTo(UPDATED_DESIRED_PROJECT_DURATION);
        assertThat(testEstimate.getDailyPrice()).isEqualTo(UPDATED_DAILY_PRICE);
        assertThat(testEstimate.getSpecificationFactor()).isEqualTo(UPDATED_SPECIFICATION_FACTOR);
        assertThat(testEstimate.getTestingFactor()).isEqualTo(UPDATED_TESTING_FACTOR);
        assertThat(testEstimate.getImplementationFactor()).isEqualTo(UPDATED_IMPLEMENTATION_FACTOR);
        assertThat(testEstimate.getSynergyBenefit()).isEqualTo(UPDATED_SYNERGY_BENEFIT);
        assertThat(testEstimate.getTotalPrice()).isEqualTo(UPDATED_TOTAL_PRICE);
        assertThat(testEstimate.getTotalDuration()).isEqualTo(UPDATED_TOTAL_DURATION);
        assertThat(testEstimate.getResourcing()).isEqualTo(UPDATED_RESOURCING);
        assertThat(testEstimate.getTotalSynergyBenefit()).isEqualTo(UPDATED_TOTAL_SYNERGY_BENEFIT);
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
