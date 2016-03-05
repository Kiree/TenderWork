package com.tol.tenderwork.web;

import com.tol.tenderwork.domain.*;
import com.tol.tenderwork.repository.EstimateRepository;
import com.tol.tenderwork.repository.ProjectRepository;
import com.tol.tenderwork.repository.RequirementRepository;
import com.tol.tenderwork.repository.TaskRepository;
import com.tol.tenderwork.repository.search.EstimateSearchRepository;
import com.tol.tenderwork.repository.search.ProjectSearchRepository;
import com.tol.tenderwork.repository.search.RequirementSearchRepository;
import com.tol.tenderwork.repository.search.TaskSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.inject.Inject;

/**
 * Created by PeHeri on 3.3.2016.
 */

/**
 * A controller for handling delete operations.
 */
@Service
@Transactional
public class DeleteController {

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

    @Autowired
    private UpdateController updateController;

    @Autowired
    private MathController mathController;

    @Autowired
    private SaveController saveController;

    @Transactional
    public void deleteTask(Long id){

        Task task = taskRepository.findOne(id);

        // Update owner requirement
        Requirement requirement = requirementRepository.findOne(task.getOwnerRequirement().getId());
        requirement.removeTask(task);
        requirement = mathController.calculateRequirement(task);
        saveController.saveRequirementToRepo(requirement);

        // Update owner estimate
        Estimate estimate = mathController.calculateEstimate(requirement.getOwnerEstimate());
        saveController.saveEstimateToRepo(estimate);

        taskRepository.delete(id);
        taskSearchRepository.delete(id);

        // Call to update owner project's edit information
        updateController.updateProject(task.getOwnerRequirement().getOwnerEstimate().getOwnerProject(), task.getOwnedBy());
    }

    @Transactional
    public void deleteEstimate(Long id){

        Estimate estimate = estimateRepository.findOne(id);

        // Delete everything that estimate owns
        for(Requirement requirement : estimate.getHasRequirementss()) {
            for(Task task : requirement.getHasTaskss()) {
                taskRepository.delete(task.getId());
                taskSearchRepository.delete(task.getId());
            }
            requirementRepository.delete(requirement.getId());
            requirementSearchRepository.delete(requirement.getId());
        }

        estimateRepository.delete(id);
        estimateSearchRepository.delete(id);

        // Call to update owner project's edit information
        updateController.updateProject(estimate.getOwnerProject(), estimate.getCreatedBy());
    }

    @Transactional
    public void deleteRequirement(Long id){

        Requirement requirement = requirementRepository.findOne(id);

        // Delete requirement's tasks
        for(Task task : requirement.getHasTaskss()) {
            taskRepository.delete(task.getId());
            taskSearchRepository.delete(task.getId());
        }

        // Update owner estimate
        Estimate estimate = estimateRepository.findOne(requirement.getOwnerEstimate().getId());
        estimate.removeRequirement(requirement);
        estimate = mathController.calculateEstimate(estimate);
        saveController.saveEstimateToRepo(estimate);

        requirementRepository.delete(id);
        requirementSearchRepository.delete(id);

        // Call to update owner project's edit information
        updateController.updateProject(requirement.getOwnerEstimate().getOwnerProject(), requirement.getOwner());
    }

    @Transactional
    public void deleteProject(Long id){

        Project project = projectRepository.findOne(id);

        // Delete everything project owns
        for(Estimate estimate : project.getHasEstimatess()) {
            deleteEstimate(estimate.getId());
        }
        projectRepository.delete(id);
        projectSearchRepository.delete(id);
    }
}
