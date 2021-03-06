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
import org.elasticsearch.index.engine.Engine;
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

    @Autowired
    private DeleteService deleteService;

    @Transactional
    public Project updateProject(Project project){
        project.setEditedDate(ZonedDateTime.now());
        return project;
    }

    // Method for updating everything, when Estimate is updated //

    @Transactional
    public Estimate updateEstimateCall(Estimate estimate) {

        log.error("UpdateEstimate aloitus:");
        // Go through all of Estimate's requirements

        Estimate estimateHelper = estimateRepository.findOne(estimate.getId());

        log.error("REQT ESTIMATECALLISSA: {}", estimateHelper.getHasRequirementss());

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
                Task resultTask = new Task();

                // Go through all of Requirement's tasks
                if(!requirementHelper.getHasTaskss().isEmpty()) {
                    for (Task task : requirementHelper.getHasTaskss()) {
                        Task taskHelper = taskRepository.findOne(task.getId());
                        task = mathService.calculateTask(taskHelper, estimate);
                        resultTask = saveService.saveTaskToRepo(task);
                        log.error("Päivitettiin task {}", task);
                    }
                    requirement = mathService.calculateRequirement(resultTask);
                }

                saveService.saveRequirementToRepo(requirement);
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
        log.error("TASK: {}", task);
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
        updateProject(task.getOwnerRequirement().getOwnerEstimate().getOwnerProject());

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
        updateProject(requirement.getOwnerEstimate().getOwnerProject());

        return result;
    }

    public Project updateProjectTags(Project project) {

        Project dbProject = projectRepository.findOne(project.getId());

        if(!project.getTags().isEmpty()) {
            for(Tag tag : project.getTags()) {
                tag.setName(tag.getName().toLowerCase());
                if(!(dbProject.getTags().contains(tag))) {
                    tag.addProject(project);
                    saveService.saveTagToRepo(tag);
                }
            }
        }

        Set<Tag> forRemoval = new HashSet<>();
        if(!dbProject.getTags().isEmpty()) {
            for (Tag tag : dbProject.getTags()) {
                if (!(project.getTags().contains(tag))) {
                    forRemoval.add(tag);
                }
            }
        }

        saveService.saveProjectToRepo(project);
        if(!forRemoval.isEmpty()) {
            for (Tag tag : forRemoval) {
                tag.removeProject(project);
                saveService.saveTagToRepo(tag);
                deleteService.deleteTag(tag.getId());
            }
        }
        return project;
    }

    public Requirement updateRequirementTags(Requirement requirement) {
        Requirement dbRequirement = requirementRepository.findOne(requirement.getId());

        if(!requirement.getTags().isEmpty()) {
            for(Tag tag : requirement.getTags()) {
                tag.setName(tag.getName().toLowerCase());
                if(!(dbRequirement.getTags().contains(tag))) {
                    tag.addRequirement(requirement);
                    saveService.saveTagToRepo(tag);
                }
            }
        }
        Set<Tag> forRemoval = new HashSet<>();
        if(!dbRequirement.getTags().isEmpty()) {
            for(Tag tag : dbRequirement.getTags()) {
                if(!(requirement.getTags().contains(tag))) {
                    forRemoval.add(tag);
                }
            }
        }

        saveService.saveRequirementToRepo(requirement);
        if(!forRemoval.isEmpty()) {
            for(Tag tag : forRemoval) {
                tag.removeRequirement(requirement);
                saveService.saveTagToRepo(tag);
                deleteService.deleteTag(tag.getId());
            }
        }
        return requirement;
    }

    public Task updateTaskTags(Task task) {
        Task dbTask = taskRepository.findOne(task.getId());

        if(!task.getTags().isEmpty()) {
            for(Tag tag : task.getTags()) {
                tag.setName(tag.getName().toLowerCase());
                if(!(dbTask.getTags().contains(tag))) {
                    tag.addTask(task);
                    saveService.saveTagToRepo(tag);
                }
            }
        }
        Set<Tag> forRemoval = new HashSet<>();
        if(!dbTask.getTags().isEmpty()) {
            for(Tag tag : dbTask.getTags()) {
                if(!(task.getTags().contains(tag))) {
                    forRemoval.add(tag);
                }
            }
        }

        saveService.saveTaskToRepo(task);
        if(!forRemoval.isEmpty()) {
            for(Tag tag : forRemoval) {
                tag.removeTask(task);
                saveService.saveTagToRepo(tag);
                deleteService.deleteTag(tag.getId());
            }
        }
        return task;
    }
}
