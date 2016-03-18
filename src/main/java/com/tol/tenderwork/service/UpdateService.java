package com.tol.tenderwork.service;

import com.tol.tenderwork.domain.*;
import com.tol.tenderwork.repository.EstimateRepository;
import com.tol.tenderwork.repository.ProjectRepository;
import com.tol.tenderwork.repository.RequirementRepository;
import com.tol.tenderwork.repository.TaskRepository;
import com.tol.tenderwork.repository.TagRepository;
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


/**
 * Created by Sebastian Körkkö on 18.2.2016.
 * Edited by Petteri Salonurmi on 19.2.2016 11:26 GMT+2.
 * Jukka was here
 */

@Service
@Transactional
public class UpdateService {

    // Logger for debugging the class //

   private final Logger log = LoggerFactory.getLogger(UpdateService.class);

    // Entity repositories //

    @Inject
    private TagRepository tagRepository;

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
    private MathService mathService;

    @Autowired
    private SaveService saveService;

    @Transactional
    public void updateProject(Project project, User user){
        project.setEditedBy(user);
        project.setEditedDate(ZonedDateTime.now());

        Project result = saveService.saveProjectToRepo(project);
    }

    // Method for updating everything, when Estimate is updated //

    @Transactional
    public Estimate updateEstimateCall(Estimate estimate) {

        log.error("UpdateEstimate aloitus:");
        // Go through all of Estimate's requirements

        Estimate estimateHelper = estimateRepository.findOne(estimate.getId());

        if (estimateHelper.getHasRequirementss().isEmpty()) {

            log.error("Estimatella ei ole Requirementteja: {}", estimateHelper);
            estimate.setResourcing(null);
            estimate.setTotalDuration(null);
            estimate.setTotalPrice(null);
            estimate.setTotalSynergyBenefit(null);

            return estimate;
        } else {

            // Kuten aiemmin, jostain syystä tämä Hash-set on aina tyhjä.
            for (Requirement requirement : estimateHelper.getHasRequirementss()) {
                Requirement requirementHelper = requirementRepository.findOne(requirement.getId());

                // Go through all of Requirement's tasks
                for (Task task : requirementHelper.getHasTaskss()) {
                    Task taskHelper = taskRepository.findOne(task.getId());
                    task = mathService.calculateTask(taskHelper, estimate);
                    Task resultTask = saveService.saveTaskToRepo(task);
                    requirement = mathService.calculateRequirement(resultTask);
                    log.error("Päivitettiin task {}", task);
                }
                Requirement resultRequirement = saveService.saveRequirementToRepo(requirement);
                estimate.addRequirement(resultRequirement);
                log.error("Päivitettiin requ {}", requirement);

            }

            log.error("UpdateEstimate lopetus");
            estimate = mathService.calculateEstimate(estimate);
            return estimate;
        }
    }

    /*
    @Transactional
    public Estimate updateEstimate(Estimate estimate) {

        //log.debug("updateAllTasks aloitus");
        //Estimate estimateHelper = estimateRepository.findOne(estimate.getId());

        Set<Requirement> requirements = estimate.getHasRequirementss();
        Set<Requirement> requirementsHelper = new HashSet<>();
        //Task taskHelper = new Task();
        //Requirement requirementHelper = new Requirement();

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
                //taskHelper = task;

                //log.debug("TASK:{}", taskHelper);

            }
            //log.debug("Taski looppi läpi");
            //requirementHelper.setHasTaskss(tasksHelper);
            // Tässä null-pointer, ei edes mene funktioon. taskHelper = null....?
            //requirementHelper = mathController.calculateRequirement(taskHelper);
            //requirementsHelper.add(requirementHelper);
            //saveController.saveRequirementToRepo(requirementHelper);
        }
        estimate.setHasRequirementss(requirementsHelper);
        //log.debug("REQHELPER: {}", requirementHelper);
        //estimate = mathController.calculateEstimate(requirementHelper, estimate);

        return estimate;
    }

    */

    // PUT/POST-methods for adding/editing entities //

    @Transactional
    public Task updateTask(Task task) {

        //Calculate the new values for the task
        task = mathService.calculateTask(task, estimateRepository.findOne(task.getOwnerRequirement().getOwnerEstimate().getId()));
        Task result = saveService.saveTaskToRepo(task);

        //Update owner requirement totals
        Requirement requirementHelper = requirementRepository.findOne(task.getOwnerRequirement().getId());
        requirementHelper.addTask(task);
        requirementHelper = mathService.calculateRequirement(task);
        saveService.saveRequirementToRepo(requirementHelper);

        //Update owner estimate totals
        Estimate estimateHelper = estimateRepository.findOne(task.getOwnerRequirement().getOwnerEstimate().getId());
        estimateHelper.addRequirement(requirementHelper);
        estimateHelper = mathService.calculateEstimate(estimateHelper);
        saveService.saveEstimateToRepo(estimateHelper);

        //Update owner project edit information
        updateProject(task.getOwnerRequirement().getOwnerEstimate().getOwnerProject(), task.getOwnedBy());

        return result;
    }

    @Transactional
    public Requirement updateRequirement(Requirement requirement) {

        //Find the owner estimate
        Estimate estimateHelper = estimateRepository.findOne(requirement.getOwnerEstimate().getId());
        estimateHelper.addRequirement(requirement);
        saveService.saveEstimateToRepo(estimateHelper);

        //Save the result of the update to the repository
        Requirement result = saveService.saveRequirementToRepo(requirement);

        //Update Project edit information
        updateProject(requirement.getOwnerEstimate().getOwnerProject(), requirement.getOwner());

        return result;
    }
}
