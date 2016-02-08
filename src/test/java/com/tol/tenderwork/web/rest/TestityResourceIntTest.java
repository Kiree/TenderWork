package com.tol.tenderwork.web.rest;

import com.tol.tenderwork.Application;
import com.tol.tenderwork.domain.Testity;
import com.tol.tenderwork.repository.TestityRepository;
import com.tol.tenderwork.repository.search.TestitySearchRepository;

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
 * Test class for the TestityResource REST controller.
 *
 * @see TestityResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class TestityResourceIntTest {

    private static final String DEFAULT_NIMI = "";
    private static final String UPDATED_NIMI = "";

    @Inject
    private TestityRepository testityRepository;

    @Inject
    private TestitySearchRepository testitySearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTestityMockMvc;

    private Testity testity;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TestityResource testityResource = new TestityResource();
        ReflectionTestUtils.setField(testityResource, "testitySearchRepository", testitySearchRepository);
        ReflectionTestUtils.setField(testityResource, "testityRepository", testityRepository);
        this.restTestityMockMvc = MockMvcBuilders.standaloneSetup(testityResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        testity = new Testity();
        testity.setNimi(DEFAULT_NIMI);
    }

    @Test
    @Transactional
    public void createTestity() throws Exception {
        int databaseSizeBeforeCreate = testityRepository.findAll().size();

        // Create the Testity

        restTestityMockMvc.perform(post("/api/testitys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(testity)))
                .andExpect(status().isCreated());

        // Validate the Testity in the database
        List<Testity> testitys = testityRepository.findAll();
        assertThat(testitys).hasSize(databaseSizeBeforeCreate + 1);
        Testity testTestity = testitys.get(testitys.size() - 1);
        assertThat(testTestity.getNimi()).isEqualTo(DEFAULT_NIMI);
    }

    @Test
    @Transactional
    public void checkNimiIsRequired() throws Exception {
        int databaseSizeBeforeTest = testityRepository.findAll().size();
        // set the field null
        testity.setNimi(null);

        // Create the Testity, which fails.

        restTestityMockMvc.perform(post("/api/testitys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(testity)))
                .andExpect(status().isBadRequest());

        List<Testity> testitys = testityRepository.findAll();
        assertThat(testitys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTestitys() throws Exception {
        // Initialize the database
        testityRepository.saveAndFlush(testity);

        // Get all the testitys
        restTestityMockMvc.perform(get("/api/testitys?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(testity.getId().intValue())))
                .andExpect(jsonPath("$.[*].nimi").value(hasItem(DEFAULT_NIMI.toString())));
    }

    @Test
    @Transactional
    public void getTestity() throws Exception {
        // Initialize the database
        testityRepository.saveAndFlush(testity);

        // Get the testity
        restTestityMockMvc.perform(get("/api/testitys/{id}", testity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(testity.getId().intValue()))
            .andExpect(jsonPath("$.nimi").value(DEFAULT_NIMI.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTestity() throws Exception {
        // Get the testity
        restTestityMockMvc.perform(get("/api/testitys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTestity() throws Exception {
        // Initialize the database
        testityRepository.saveAndFlush(testity);

		int databaseSizeBeforeUpdate = testityRepository.findAll().size();

        // Update the testity
        testity.setNimi(UPDATED_NIMI);

        restTestityMockMvc.perform(put("/api/testitys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(testity)))
                .andExpect(status().isOk());

        // Validate the Testity in the database
        List<Testity> testitys = testityRepository.findAll();
        assertThat(testitys).hasSize(databaseSizeBeforeUpdate);
        Testity testTestity = testitys.get(testitys.size() - 1);
        assertThat(testTestity.getNimi()).isEqualTo(UPDATED_NIMI);
    }

    @Test
    @Transactional
    public void deleteTestity() throws Exception {
        // Initialize the database
        testityRepository.saveAndFlush(testity);

		int databaseSizeBeforeDelete = testityRepository.findAll().size();

        // Get the testity
        restTestityMockMvc.perform(delete("/api/testitys/{id}", testity.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Testity> testitys = testityRepository.findAll();
        assertThat(testitys).hasSize(databaseSizeBeforeDelete - 1);
    }
}
