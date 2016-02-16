package com.tol.tenderwork.web.rest;

import com.tol.tenderwork.Application;
import com.tol.tenderwork.domain.Task;
import com.tol.tenderwork.repository.TaskRepository;
import com.tol.tenderwork.repository.search.TaskSearchRepository;

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
 * Test class for the TaskResource REST controller.
 *
 * @see TaskResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class TaskResourceIntTest {

    private static final String DEFAULT_NAME = "A";
    private static final String UPDATED_NAME = "B";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final Integer DEFAULT_ESTIMATE_SPECIFICATION = 0;
    private static final Integer UPDATED_ESTIMATE_SPECIFICATION = 1;

    private static final Integer DEFAULT_ESTIMATE_IMPLEMENTATION = 0;
    private static final Integer UPDATED_ESTIMATE_IMPLEMENTATION = 1;

    private static final Integer DEFAULT_ESTIMATE_TESTING = 0;
    private static final Integer UPDATED_ESTIMATE_TESTING = 1;

    private static final Integer DEFAULT_ESTIMATE_SYNERGY = 0;
    private static final Integer UPDATED_ESTIMATE_SYNERGY = 1;

    private static final Boolean DEFAULT_SYNERGY_CHECK = false;
    private static final Boolean UPDATED_SYNERGY_CHECK = true;

    private static final Integer DEFAULT_SYNERGY_TOTAL = 0;
    private static final Integer UPDATED_SYNERGY_TOTAL = 1;

    private static final Integer DEFAULT_ESTIMATE_TOTAL = 0;
    private static final Integer UPDATED_ESTIMATE_TOTAL = 1;

    @Inject
    private TaskRepository taskRepository;

    @Inject
    private TaskSearchRepository taskSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTaskMockMvc;

    private Task task;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TaskResource taskResource = new TaskResource();
        ReflectionTestUtils.setField(taskResource, "taskSearchRepository", taskSearchRepository);
        ReflectionTestUtils.setField(taskResource, "taskRepository", taskRepository);
        this.restTaskMockMvc = MockMvcBuilders.standaloneSetup(taskResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        task = new Task();
        task.setName(DEFAULT_NAME);
        task.setDescription(DEFAULT_DESCRIPTION);
        task.setEstimateSpecification(DEFAULT_ESTIMATE_SPECIFICATION);
        task.setEstimateImplementation(DEFAULT_ESTIMATE_IMPLEMENTATION);
        task.setEstimateTesting(DEFAULT_ESTIMATE_TESTING);
        task.setEstimateSynergy(DEFAULT_ESTIMATE_SYNERGY);
        task.setSynergyCheck(DEFAULT_SYNERGY_CHECK);
        task.setSynergyTotal(DEFAULT_SYNERGY_TOTAL);
        task.setEstimateTotal(DEFAULT_ESTIMATE_TOTAL);
    }

    @Test
    @Transactional
    public void createTask() throws Exception {
        int databaseSizeBeforeCreate = taskRepository.findAll().size();

        // Create the Task

        restTaskMockMvc.perform(post("/api/tasks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(task)))
                .andExpect(status().isCreated());

        // Validate the Task in the database
        List<Task> tasks = taskRepository.findAll();
        assertThat(tasks).hasSize(databaseSizeBeforeCreate + 1);
        Task testTask = tasks.get(tasks.size() - 1);
        assertThat(testTask.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTask.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTask.getEstimateSpecification()).isEqualTo(DEFAULT_ESTIMATE_SPECIFICATION);
        assertThat(testTask.getEstimateImplementation()).isEqualTo(DEFAULT_ESTIMATE_IMPLEMENTATION);
        assertThat(testTask.getEstimateTesting()).isEqualTo(DEFAULT_ESTIMATE_TESTING);
        assertThat(testTask.getEstimateSynergy()).isEqualTo(DEFAULT_ESTIMATE_SYNERGY);
        assertThat(testTask.getSynergyCheck()).isEqualTo(DEFAULT_SYNERGY_CHECK);
        assertThat(testTask.getSynergyTotal()).isEqualTo(DEFAULT_SYNERGY_TOTAL);
        assertThat(testTask.getEstimateTotal()).isEqualTo(DEFAULT_ESTIMATE_TOTAL);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = taskRepository.findAll().size();
        // set the field null
        task.setName(null);

        // Create the Task, which fails.

        restTaskMockMvc.perform(post("/api/tasks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(task)))
                .andExpect(status().isBadRequest());

        List<Task> tasks = taskRepository.findAll();
        assertThat(tasks).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEstimateSpecificationIsRequired() throws Exception {
        int databaseSizeBeforeTest = taskRepository.findAll().size();
        // set the field null
        task.setEstimateSpecification(null);

        // Create the Task, which fails.

        restTaskMockMvc.perform(post("/api/tasks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(task)))
                .andExpect(status().isBadRequest());

        List<Task> tasks = taskRepository.findAll();
        assertThat(tasks).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEstimateImplementationIsRequired() throws Exception {
        int databaseSizeBeforeTest = taskRepository.findAll().size();
        // set the field null
        task.setEstimateImplementation(null);

        // Create the Task, which fails.

        restTaskMockMvc.perform(post("/api/tasks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(task)))
                .andExpect(status().isBadRequest());

        List<Task> tasks = taskRepository.findAll();
        assertThat(tasks).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEstimateTestingIsRequired() throws Exception {
        int databaseSizeBeforeTest = taskRepository.findAll().size();
        // set the field null
        task.setEstimateTesting(null);

        // Create the Task, which fails.

        restTaskMockMvc.perform(post("/api/tasks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(task)))
                .andExpect(status().isBadRequest());

        List<Task> tasks = taskRepository.findAll();
        assertThat(tasks).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSynergyCheckIsRequired() throws Exception {
        int databaseSizeBeforeTest = taskRepository.findAll().size();
        // set the field null
        task.setSynergyCheck(null);

        // Create the Task, which fails.

        restTaskMockMvc.perform(post("/api/tasks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(task)))
                .andExpect(status().isBadRequest());

        List<Task> tasks = taskRepository.findAll();
        assertThat(tasks).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTasks() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the tasks
        restTaskMockMvc.perform(get("/api/tasks?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(task.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].estimateSpecification").value(hasItem(DEFAULT_ESTIMATE_SPECIFICATION)))
                .andExpect(jsonPath("$.[*].estimateImplementation").value(hasItem(DEFAULT_ESTIMATE_IMPLEMENTATION)))
                .andExpect(jsonPath("$.[*].estimateTesting").value(hasItem(DEFAULT_ESTIMATE_TESTING)))
                .andExpect(jsonPath("$.[*].estimateSynergy").value(hasItem(DEFAULT_ESTIMATE_SYNERGY)))
                .andExpect(jsonPath("$.[*].synergyCheck").value(hasItem(DEFAULT_SYNERGY_CHECK.booleanValue())))
                .andExpect(jsonPath("$.[*].synergyTotal").value(hasItem(DEFAULT_SYNERGY_TOTAL)))
                .andExpect(jsonPath("$.[*].estimateTotal").value(hasItem(DEFAULT_ESTIMATE_TOTAL)));
    }

    @Test
    @Transactional
    public void getTask() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get the task
        restTaskMockMvc.perform(get("/api/tasks/{id}", task.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(task.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.estimateSpecification").value(DEFAULT_ESTIMATE_SPECIFICATION))
            .andExpect(jsonPath("$.estimateImplementation").value(DEFAULT_ESTIMATE_IMPLEMENTATION))
            .andExpect(jsonPath("$.estimateTesting").value(DEFAULT_ESTIMATE_TESTING))
            .andExpect(jsonPath("$.estimateSynergy").value(DEFAULT_ESTIMATE_SYNERGY))
            .andExpect(jsonPath("$.synergyCheck").value(DEFAULT_SYNERGY_CHECK.booleanValue()))
            .andExpect(jsonPath("$.synergyTotal").value(DEFAULT_SYNERGY_TOTAL))
            .andExpect(jsonPath("$.estimateTotal").value(DEFAULT_ESTIMATE_TOTAL));
    }

    @Test
    @Transactional
    public void getNonExistingTask() throws Exception {
        // Get the task
        restTaskMockMvc.perform(get("/api/tasks/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTask() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

		int databaseSizeBeforeUpdate = taskRepository.findAll().size();

        // Update the task
        task.setName(UPDATED_NAME);
        task.setDescription(UPDATED_DESCRIPTION);
        task.setEstimateSpecification(UPDATED_ESTIMATE_SPECIFICATION);
        task.setEstimateImplementation(UPDATED_ESTIMATE_IMPLEMENTATION);
        task.setEstimateTesting(UPDATED_ESTIMATE_TESTING);
        task.setEstimateSynergy(UPDATED_ESTIMATE_SYNERGY);
        task.setSynergyCheck(UPDATED_SYNERGY_CHECK);
        task.setSynergyTotal(UPDATED_SYNERGY_TOTAL);
        task.setEstimateTotal(UPDATED_ESTIMATE_TOTAL);

        restTaskMockMvc.perform(put("/api/tasks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(task)))
                .andExpect(status().isOk());

        // Validate the Task in the database
        List<Task> tasks = taskRepository.findAll();
        assertThat(tasks).hasSize(databaseSizeBeforeUpdate);
        Task testTask = tasks.get(tasks.size() - 1);
        assertThat(testTask.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTask.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTask.getEstimateSpecification()).isEqualTo(UPDATED_ESTIMATE_SPECIFICATION);
        assertThat(testTask.getEstimateImplementation()).isEqualTo(UPDATED_ESTIMATE_IMPLEMENTATION);
        assertThat(testTask.getEstimateTesting()).isEqualTo(UPDATED_ESTIMATE_TESTING);
        assertThat(testTask.getEstimateSynergy()).isEqualTo(UPDATED_ESTIMATE_SYNERGY);
        assertThat(testTask.getSynergyCheck()).isEqualTo(UPDATED_SYNERGY_CHECK);
        assertThat(testTask.getSynergyTotal()).isEqualTo(UPDATED_SYNERGY_TOTAL);
        assertThat(testTask.getEstimateTotal()).isEqualTo(UPDATED_ESTIMATE_TOTAL);
    }

    @Test
    @Transactional
    public void deleteTask() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

		int databaseSizeBeforeDelete = taskRepository.findAll().size();

        // Get the task
        restTaskMockMvc.perform(delete("/api/tasks/{id}", task.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Task> tasks = taskRepository.findAll();
        assertThat(tasks).hasSize(databaseSizeBeforeDelete - 1);
    }
}
