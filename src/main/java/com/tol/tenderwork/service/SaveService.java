package com.tol.tenderwork.service;

import com.tol.tenderwork.domain.*;
import com.tol.tenderwork.repository.*;
import com.tol.tenderwork.repository.search.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by PeHeri on 3.3.2016.
 */

/**
 * A simple controller for saving entities into their respective repositories.
 */
@Service
@Transactional
public class SaveService {

    // Entity repositories //

    @Inject
    private TaskRepository taskRepository;

    @Inject
    private TaskSearchRepository taskSearchRepository;

    @Inject
    private RequirementRepository requirementRepository;

    @Inject
    private RequirementSearchRepository requirementSearchRepository;

    @Inject
    private EstimateRepository estimateRepository;

    @Inject
    private EstimateSearchRepository estimateSearchRepository;

    @Inject
    private ProjectRepository projectRepository;

    @Inject
    private ProjectSearchRepository projectSearchRepository;

    @Inject
    private TagRepository tagRepository;

    @Inject
    private TagSearchRepository tagSearchRepository;

    // Save entity to repo-methods //

    @Transactional
    public Task saveTaskToRepo(Task task) {
        Task result = taskRepository.save(task);
        taskSearchRepository.save(result);

        return result;
    }

    @Transactional
    public Requirement saveRequirementToRepo(Requirement requirement) {
        Requirement result = requirementRepository.save(requirement);
        requirementSearchRepository.save(result);

        return result;
    }

    @Transactional
    public Estimate saveEstimateToRepo(Estimate estimate) {
        Estimate result = estimateRepository.save(estimate);
        estimateSearchRepository.save(result);

        return result;
    }

    @Transactional
    public Project saveProjectToRepo(Project project) {
        Project result = projectRepository.save(project);
        projectSearchRepository.save(result);

        for (Tag tag : project.getHasTagss()){
            //tag.addProject(project);
            saveTagToRepo(tag);
            project.addTag(tag);
        }

        return result;
    }

    @Transactional
    public Tag saveTagToRepo(Tag tag) {

        Tag result = tagRepository.save(tag);
        tagSearchRepository.save(result);
    return result;
        }

}
