package com.tol.tenderwork.web.rest;

import com.tol.tenderwork.Application;
import com.tol.tenderwork.domain.Requirement;
import com.tol.tenderwork.repository.RequirementRepository;
import com.tol.tenderwork.repository.search.RequirementSearchRepository;

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
 * Test class for the RequirementResource REST controller.
 *
 * @see RequirementResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class RequirementResourceIntTest {

    private static final String DEFAULT_NAME = "A";
    private static final String UPDATED_NAME = "B";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final Float DEFAULT_TOTAL_DURATION = 0F;
    private static final Float UPDATED_TOTAL_DURATION = 1F;

    private static final Float DEFAULT_DURATION_SPECIFICATION = 0F;
    private static final Float UPDATED_DURATION_SPECIFICATION = 1F;

    private static final Float DEFAULT_DURATION_IMPLEMENTATION = 0F;
    private static final Float UPDATED_DURATION_IMPLEMENTATION = 1F;

    private static final Float DEFAULT_DURATION_TESTING = 0F;
    private static final Float UPDATED_DURATION_TESTING = 1F;

    private static final Float DEFAULT_SYNERGY_BENEFIT = 0F;
    private static final Float UPDATED_SYNERGY_BENEFIT = 1F;

    @Inject
    private RequirementRepository requirementRepository;

    @Inject
    private RequirementSearchRepository requirementSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restRequirementMockMvc;

    private Requirement requirement;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RequirementResource requirementResource = new RequirementResource();
        ReflectionTestUtils.setField(requirementResource, "requirementSearchRepository", requirementSearchRepository);
        ReflectionTestUtils.setField(requirementResource, "requirementRepository", requirementRepository);
        this.restRequirementMockMvc = MockMvcBuilders.standaloneSetup(requirementResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        requirement = new Requirement();
        requirement.setName(DEFAULT_NAME);
        requirement.setDescription(DEFAULT_DESCRIPTION);
        requirement.setTotalDuration(DEFAULT_TOTAL_DURATION);
        requirement.setDurationSpecification(DEFAULT_DURATION_SPECIFICATION);
        requirement.setDurationImplementation(DEFAULT_DURATION_IMPLEMENTATION);
        requirement.setDurationTesting(DEFAULT_DURATION_TESTING);
        requirement.setSynergyBenefit(DEFAULT_SYNERGY_BENEFIT);
    }

    @Test
    @Transactional
    public void createRequirement() throws Exception {
        int databaseSizeBeforeCreate = requirementRepository.findAll().size();

        // Create the Requirement

        restRequirementMockMvc.perform(post("/api/requirements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(requirement)))
                .andExpect(status().isCreated());

        // Validate the Requirement in the database
        List<Requirement> requirements = requirementRepository.findAll();
        assertThat(requirements).hasSize(databaseSizeBeforeCreate + 1);
        Requirement testRequirement = requirements.get(requirements.size() - 1);
        assertThat(testRequirement.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRequirement.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testRequirement.getTotalDuration()).isEqualTo(DEFAULT_TOTAL_DURATION);
        assertThat(testRequirement.getDurationSpecification()).isEqualTo(DEFAULT_DURATION_SPECIFICATION);
        assertThat(testRequirement.getDurationImplementation()).isEqualTo(DEFAULT_DURATION_IMPLEMENTATION);
        assertThat(testRequirement.getDurationTesting()).isEqualTo(DEFAULT_DURATION_TESTING);
        assertThat(testRequirement.getSynergyBenefit()).isEqualTo(DEFAULT_SYNERGY_BENEFIT);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = requirementRepository.findAll().size();
        // set the field null
        requirement.setName(null);

        // Create the Requirement, which fails.

        restRequirementMockMvc.perform(post("/api/requirements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(requirement)))
                .andExpect(status().isBadRequest());

        List<Requirement> requirements = requirementRepository.findAll();
        assertThat(requirements).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRequirements() throws Exception {
        // Initialize the database
        requirementRepository.saveAndFlush(requirement);

        // Get all the requirements
        restRequirementMockMvc.perform(get("/api/requirements?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(requirement.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].totalDuration").value(hasItem(DEFAULT_TOTAL_DURATION)))
                .andExpect(jsonPath("$.[*].durationSpecification").value(hasItem(DEFAULT_DURATION_SPECIFICATION)))
                .andExpect(jsonPath("$.[*].durationImplementation").value(hasItem(DEFAULT_DURATION_IMPLEMENTATION)))
                .andExpect(jsonPath("$.[*].durationTesting").value(hasItem(DEFAULT_DURATION_TESTING)))
                .andExpect(jsonPath("$.[*].synergyBenefit").value(hasItem(DEFAULT_SYNERGY_BENEFIT)));
    }

    @Test
    @Transactional
    public void getRequirement() throws Exception {
        // Initialize the database
        requirementRepository.saveAndFlush(requirement);

        // Get the requirement
        restRequirementMockMvc.perform(get("/api/requirements/{id}", requirement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(requirement.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.totalDuration").value(DEFAULT_TOTAL_DURATION))
            .andExpect(jsonPath("$.durationSpecification").value(DEFAULT_DURATION_SPECIFICATION))
            .andExpect(jsonPath("$.durationImplementation").value(DEFAULT_DURATION_IMPLEMENTATION))
            .andExpect(jsonPath("$.durationTesting").value(DEFAULT_DURATION_TESTING))
            .andExpect(jsonPath("$.synergyBenefit").value(DEFAULT_SYNERGY_BENEFIT));
    }

    @Test
    @Transactional
    public void getNonExistingRequirement() throws Exception {
        // Get the requirement
        restRequirementMockMvc.perform(get("/api/requirements/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRequirement() throws Exception {
        // Initialize the database
        requirementRepository.saveAndFlush(requirement);

		int databaseSizeBeforeUpdate = requirementRepository.findAll().size();

        // Update the requirement
        requirement.setName(UPDATED_NAME);
        requirement.setDescription(UPDATED_DESCRIPTION);
        requirement.setTotalDuration(UPDATED_TOTAL_DURATION);
        requirement.setDurationSpecification(UPDATED_DURATION_SPECIFICATION);
        requirement.setDurationImplementation(UPDATED_DURATION_IMPLEMENTATION);
        requirement.setDurationTesting(UPDATED_DURATION_TESTING);
        requirement.setSynergyBenefit(UPDATED_SYNERGY_BENEFIT);

        restRequirementMockMvc.perform(put("/api/requirements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(requirement)))
                .andExpect(status().isOk());

        // Validate the Requirement in the database
        List<Requirement> requirements = requirementRepository.findAll();
        assertThat(requirements).hasSize(databaseSizeBeforeUpdate);
        Requirement testRequirement = requirements.get(requirements.size() - 1);
        assertThat(testRequirement.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRequirement.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRequirement.getTotalDuration()).isEqualTo(UPDATED_TOTAL_DURATION);
        assertThat(testRequirement.getDurationSpecification()).isEqualTo(UPDATED_DURATION_SPECIFICATION);
        assertThat(testRequirement.getDurationImplementation()).isEqualTo(UPDATED_DURATION_IMPLEMENTATION);
        assertThat(testRequirement.getDurationTesting()).isEqualTo(UPDATED_DURATION_TESTING);
        assertThat(testRequirement.getSynergyBenefit()).isEqualTo(UPDATED_SYNERGY_BENEFIT);
    }

    @Test
    @Transactional
    public void deleteRequirement() throws Exception {
        // Initialize the database
        requirementRepository.saveAndFlush(requirement);

		int databaseSizeBeforeDelete = requirementRepository.findAll().size();

        // Get the requirement
        restRequirementMockMvc.perform(delete("/api/requirements/{id}", requirement.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Requirement> requirements = requirementRepository.findAll();
        assertThat(requirements).hasSize(databaseSizeBeforeDelete - 1);
    }
}
