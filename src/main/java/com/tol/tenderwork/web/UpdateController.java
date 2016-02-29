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

    //private final Logger log = LoggerFactory.getLogger(UpdateController.class);

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

    @Transactional
    public void updateProject(Project project, User user){
        project.setEditedBy(user);
        project.setEditedDate(ZonedDateTime.now());

        Project result = projectRepository.save(project);
        projectSearchRepository.save(result);
    }

    // Methods that calculate changes to entities //

    @Transactional
    public Estimate calculateEstimate(Requirement requirement, Estimate estimateHelper){
        Estimate estimate = estimateRepository.findOne(requirement.getOwnerEstimate().getId());
        estimate.setSpecificationFactor(estimateHelper.getSpecificationFactor());
        estimate.setImplementationFactor(estimateHelper.getImplementationFactor());
        estimate.setTestingFactor(estimateHelper.getTestingFactor());
        estimate.setSynergyBenefit(estimateHelper.getSynergyBenefit());

        Set<Requirement> requirements = estimate.getHasRequirementss();
        float totalDurationHelper = 0;
        float totalSynergyHelper = 0;

        for(Requirement r : requirements) {
            if(r.getTotalDuration() != null) {
                totalDurationHelper = totalDurationHelper + r.getTotalDuration();
            }
            if(r.getSynergyBenefit() != null) {
                totalSynergyHelper = totalSynergyHelper + r.getSynergyBenefit();
            }
        }

        if(totalSynergyHelper > 0 || estimate.getSynergyBenefit() > 0) {
            estimate.setTotalPrice((long)Math.round((totalDurationHelper - totalSynergyHelper) * estimate.getDailyPrice()));
            estimate.setTotalDuration(totalDurationHelper - totalSynergyHelper);
            estimate.setResourcing(estimate.getTotalDuration() / (estimate.getWorkdaysInMonth() * estimate.getWorkdaysInMonth()));
            estimate.setTotalSynergyBenefit(totalSynergyHelper);
        } else {
            estimate.setTotalPrice((long) totalDurationHelper * estimate.getDailyPrice());
            estimate.setResourcing(totalDurationHelper / (estimate.getWorkdaysInMonth() * estimate.getDesiredProjectDuration()));
            estimate.setTotalDuration(totalDurationHelper);
            estimate.setTotalSynergyBenefit((float)0);
        }
        return estimate;
    }

    @Transactional
    public Requirement calculateRequirement(Task task){
        Requirement requirement = requirementRepository.findOne(task.getOwnerRequirement().getId());
        Set<Task> tasks = requirement.getHasTaskss();
        float totalDurationHelper = 0;
        float totalSpecificationHelper = 0;
        float totalImplementationHelper = 0;
        float totalTestingHelper = 0;
        float totalSynergyHelper = 0;

        for (Task t : tasks) {
            totalDurationHelper = totalDurationHelper + t.getEstimateTotal();
            totalSpecificationHelper = totalSpecificationHelper + t.getSpecificationTotal();
            totalImplementationHelper = totalImplementationHelper + t.getImplementationTotal();
            totalTestingHelper = totalTestingHelper + t.getTestingTotal();
            totalSynergyHelper = totalSynergyHelper + t.getSynergyTotal();
        }

        requirement.setTotalDuration(totalDurationHelper);
        requirement.setDurationSpecification(totalSpecificationHelper);
        requirement.setDurationImplementation(totalImplementationHelper);
        requirement.setDurationTesting(totalTestingHelper);
        requirement.setSynergyBenefit(totalSynergyHelper);

    return requirement;
    }

    @Transactional
    public Task calculateTask(Task task, Estimate estimate) {

        task.setImplementationFactor(estimate);
        task.setSpecificationFactor(estimate);
        task.setSpecificationFactor(estimate);
        task.setSynergyBenefit(estimate);

        task.setSynergyTotal((float)0);
        if (task.getSynergyCheck() && task.getEstimateSynergy() != null && task.getEstimateSynergy() > 0) {
            float synergyHelper = task.getEstimateSynergy() * estimate.getSynergyBenefit();
            task.setSynergyTotal(synergyHelper);
        }

        task.setSpecificationTotal((float)task.getEstimateSpecification());
        if (estimate.getSpecificationFactor() > 0 && task.getEstimateSpecification() > 0) {
            float specFactorHelper = task.getEstimateSpecification() * estimate.getSpecificationFactor();
            task.setSpecificationTotal(specFactorHelper);
        } else if(estimate.getSpecificationFactor() == 0) {
            task.setSpecificationTotal((float)0);
        }

        task.setImplementationTotal((float)task.getEstimateImplementation());
        if (estimate.getImplementationFactor() > 0 && task.getEstimateImplementation() > 0) {
            float impFactorHelper = task.getEstimateImplementation() * estimate.getImplementationFactor();
            task.setImplementationTotal(impFactorHelper);
        } else if(estimate.getImplementationFactor() == 0) {
            task.setImplementationTotal((float)0);
        }

        task.setTestingTotal((float)task.getEstimateTesting());
        if(estimate.getTestingFactor() > 0 && task.getEstimateImplementation() > 0){
            float testFactorHelper = task.getEstimateTesting() * estimate.getTestingFactor();
            task.setTestingTotal(testFactorHelper);
        } else if(estimate.getTestingFactor() == 0) {
            task.setTestingTotal((float)0);
        }

        task.setEstimateTotal(task.getSpecificationTotal() + task.getImplementationTotal() + task.getTestingTotal());

        return task;
    }

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
        //log.debug("ESTIMATE SAVED: {}", result.getId());

        return result;
    }

    // Method for updating everything, when Estimate is updated //

    @Transactional
    public Estimate updateAllTasks(Estimate estimate) {

        Estimate estimateHelper = estimateRepository.findOne(estimate.getId());
        Set<Requirement> requirements = estimateHelper.getHasRequirementss();
        Set<Requirement> requirementsHelper = new HashSet<>();
        Task taskHelper = new Task();
        Requirement requirementHelper = new Requirement();

        //log.debug("REQS: {}", requirements);

        for(Requirement requirement : requirements){
            Set<Task> tasks = requirement.getHasTaskss();
            Set<Task> tasksHelper = new HashSet<>();

            //log.debug("TASKS: {}", tasks);

            for(Task task : tasks){
                Task taskResult = calculateTask(task, estimate);
                saveTaskToRepo(taskResult);
                tasksHelper.add(task);
                taskHelper = task;

                //log.debug("TASK:{}", taskHelper);

            }
            requirementHelper.setHasTaskss(tasksHelper);
            requirementHelper = calculateRequirement(taskHelper);
            requirementsHelper.add(requirementHelper);
            saveRequirementToRepo(requirementHelper);
        }
        estimate.setHasRequirementss(requirementsHelper);
        //log.debug("REQHELPER: {}", requirementHelper);
        estimate = calculateEstimate(requirementHelper, estimate);
        Estimate result = saveEstimateToRepo(estimate);

        return result;
    }

    // PUT/POST-methods for adding/updating entities //

    @Transactional
    public Task modifyTask(Task task) {

        task = calculateTask(task, estimateRepository.findOne(task.getOwnerRequirement().getOwnerEstimate().getId()));
        Task result = saveTaskToRepo(task);

        Requirement requirementHelper = requirementRepository.findOne(task.getOwnerRequirement().getId());
        requirementHelper.addTask(task);
        requirementHelper = calculateRequirement(task);
        saveRequirementToRepo(requirementHelper);

        Estimate estimateHelper = estimateRepository.findOne(task.getOwnerRequirement().getOwnerEstimate().getId());
        estimateHelper.addRequirement(requirementHelper);
        estimateHelper = calculateEstimate(requirementHelper, estimateHelper);
        saveEstimateToRepo(estimateHelper);

        updateProject(task.getOwnerRequirement().getOwnerEstimate().getOwnerProject(), task.getOwnedBy());

        return result;
    }

    @Transactional
    public Requirement modifyRequirement(Requirement requirement) {
        Estimate estimate = estimateRepository.findOne(requirement.getOwnerEstimate().getId());
        estimate.addRequirement(requirement);
        saveEstimateToRepo(estimate);

        Requirement result = saveRequirementToRepo(requirement);

        updateProject(requirement.getOwnerEstimate().getOwnerProject(), requirement.getOwner());

        return result;
    }
}
