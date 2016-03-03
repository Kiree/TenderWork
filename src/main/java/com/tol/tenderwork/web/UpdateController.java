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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;


/**
 * Created by Sebastian Körkkö on 18.2.2016.
 * Edited by Petteri Salonurmi on 19.2.2016 11:26 GMT+2.
 * Jukka was here
 */

@Service
@Transactional
public class UpdateController {

    // Logger for debugging the class //

   // private final Logger log = LoggerFactory.getLogger(UpdateController.class);

    // Entity repositories //

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
    private TaskRepository taskRepository;

    @Inject
    private TaskSearchRepository taskSearchRepository;

    @Autowired
    private MathController mathController;

    @Autowired
    private SaveController saveController;

    @Transactional
    public void updateProject(Project project, User user){
        project.setEditedBy(user);
        project.setEditedDate(ZonedDateTime.now());

        Project result = projectRepository.save(project);
        projectSearchRepository.save(result);
    }

    // Method for updating everything, when Estimate is updated //

    @Transactional
    public void updateAllTasks(Estimate estimate) {

        //log.debug("updateAllTasks aloitus");
        Estimate estimateHelper = estimateRepository.findOne(estimate.getId());
        Set<Requirement> requirements = estimateHelper.getHasRequirementss();
        Set<Requirement> requirementsHelper = new HashSet<>();
        Task taskHelper = new Task();
        Requirement requirementHelper = new Requirement();

        //log.debug("REQS: {}", requirements);

        for(Requirement requirement : requirements){
            //log.error("Requ looppi");
            Set<Task> tasks = requirement.getHasTaskss();
            Set<Task> tasksHelper = new HashSet<>();

            //log.debug("TASKS: {}", tasks);

            for(Task task : tasks){
                //log.error("Taski looppi");
                Task taskResult = mathController.calculateTask(task, estimate);
                saveController.saveTaskToRepo(taskResult);
                tasksHelper.add(task);
                taskHelper = task;

                //log.debug("TASK:{}", taskHelper);

            }
            //log.debug("Taski looppi läpi");
            requirementHelper.setHasTaskss(tasksHelper);
            // Tässä null-pointer, ei edes mene funktioon. taskHelper = null....?
            requirementHelper = mathController.calculateRequirement(taskHelper);
            requirementsHelper.add(requirementHelper);
            saveController.saveRequirementToRepo(requirementHelper);
        }
        estimate.setHasRequirementss(requirementsHelper);
        //log.debug("REQHELPER: {}", requirementHelper);
        estimate = mathController.calculateEstimate(requirementHelper, estimate);
    }

    // PUT/POST-methods for adding/updating entities //

    @Transactional
    public Task modifyTask(Task task) {

        task = mathController.calculateTask(task, estimateRepository.findOne(task.getOwnerRequirement().getOwnerEstimate().getId()));
        Task result = saveController.saveTaskToRepo(task);

        Requirement requirementHelper = requirementRepository.findOne(task.getOwnerRequirement().getId());
        requirementHelper.addTask(task);
        requirementHelper = mathController.calculateRequirement(task);
        saveController.saveRequirementToRepo(requirementHelper);

        Estimate estimateHelper = estimateRepository.findOne(task.getOwnerRequirement().getOwnerEstimate().getId());
        estimateHelper.addRequirement(requirementHelper);
        estimateHelper = mathController.calculateEstimate(requirementHelper, estimateHelper);
        saveController.saveEstimateToRepo(estimateHelper);

        updateProject(task.getOwnerRequirement().getOwnerEstimate().getOwnerProject(), task.getOwnedBy());

        return result;
    }

    @Transactional
    public Requirement modifyRequirement(Requirement requirement) {
        Estimate estimate = estimateRepository.findOne(requirement.getOwnerEstimate().getId());
        estimate.addRequirement(requirement);
        saveController.saveEstimateToRepo(estimate);

        Requirement result = saveController.saveRequirementToRepo(requirement);

        updateProject(requirement.getOwnerEstimate().getOwnerProject(), requirement.getOwner());

        return result;
    }
}
