package com.tol.tenderwork.web;

import com.tol.tenderwork.domain.*;
import com.tol.tenderwork.repository.EstimateRepository;
import com.tol.tenderwork.repository.ProjectRepository;
import com.tol.tenderwork.repository.RequirementRepository;
import com.tol.tenderwork.repository.search.EstimateSearchRepository;
import com.tol.tenderwork.repository.search.ProjectSearchRepository;
import com.tol.tenderwork.repository.search.RequirementSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.Set;


/**
 * Created by Sebastian Körkkö on 18.2.2016.
 * Edited by Petteri Salonurmi on 19.2.2016 11:26 GMT+2.
 * Jukka was here
 */

@Service
@Transactional
public class UpdateController {

    private final Logger log = LoggerFactory.getLogger(UpdateController.class);

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


    @Transactional
    public void updateProject(Project project, User user){
        project.setEditedBy(user);
        project.setEditedDate(ZonedDateTime.now());

        Project result = projectRepository.save(project);
        projectSearchRepository.save(result);
    }

    @Transactional
    public void updateEstimate(Requirement requirement){
        Estimate estimate = estimateRepository.findOne(requirement.getOwnerEstimate().getId());
        Set<Requirement> requirements = estimate.getHasRequirementss();
        float totalDurationHelper = 0;
        float totalSynergyHelper = 0;

        log.debug("REQS: ", requirements);

        for(Requirement r : requirements) {
            if(r.getTotalDuration() != null) {
                totalDurationHelper = totalDurationHelper + r.getTotalDuration();
            }
            if(r.getSynergyBenefit() != null) {
                totalSynergyHelper = totalSynergyHelper + r.getSynergyBenefit();
            }
        }
        totalDurationHelper  = Math.round(totalDurationHelper);

        if(totalSynergyHelper != 0 || estimate.getSynergyBenefit() != 0) {
            totalSynergyHelper = Math.round(totalSynergyHelper);
            estimate.setTotalPrice(((long) totalDurationHelper - (long)totalSynergyHelper) * estimate.getDailyPrice());
            estimate.setTotalDuration(totalDurationHelper - totalSynergyHelper);
            estimate.setResourcing(estimate.getTotalDuration() / (estimate.getWorkdaysInMonth() * estimate.getWorkdaysInMonth()));
            estimate.setTotalSynergyBenefit(totalSynergyHelper);
        } else {
            estimate.setTotalPrice((long) totalDurationHelper * estimate.getDailyPrice());
            estimate.setResourcing(totalDurationHelper / (estimate.getWorkdaysInMonth() * estimate.getDesiredProjectDuration()));
            estimate.setTotalDuration(totalDurationHelper);
            estimate.setTotalSynergyBenefit((float)0);
        }

        Estimate result = estimateRepository.save(estimate);
        estimateSearchRepository.save(result);
    }

    @Transactional
    public void updateRequirement(Task task){
        Requirement requirement = requirementRepository.findOne(task.getOwnerRequirement().getId());
        Set<Task> tasks = requirement.getHasTaskss();
        float totalDurationHelper = 0;
        float totalSpecificationHelper = 0;
        float totalImplementationHelper = 0;
        float totalTestingHelper = 0;
        float totalSynergyHelper = 0;
        /*
        *log.debug("Spechelper", specHelper);
        *log.debug("Task", requirement);
        */
        for (Task t : tasks) {
            totalDurationHelper = totalDurationHelper + t.getEstimateTotal();
            totalSpecificationHelper = totalSpecificationHelper + t.getEstimateSpecification();
            totalImplementationHelper = totalImplementationHelper + t.getEstimateImplementation();
            totalTestingHelper = totalTestingHelper + t.getEstimateTesting();
            totalSynergyHelper = totalSynergyHelper + t.getSynergyTotal();

            //log.debug("IMP", task.getEstimateImplementation());
            //log.debug("TOTAL", task.getEstimateTotal());
        }

        requirement.setTotalDuration(totalDurationHelper);
        requirement.setDurationSpecification(totalSpecificationHelper);
        requirement.setDurationImplementation(totalImplementationHelper);
        requirement.setDurationTesting(totalTestingHelper);
        requirement.setSynergyBenefit(totalSynergyHelper);

        Requirement result = requirementRepository.save(requirement);
        requirementSearchRepository.save(result);

    }

    @Transactional
    public Task updateTask(Task task) {

        task.setSynergyTotal((float)0);
        if (task.getSynergyCheck() == true && task.getEstimateSynergy() != null && task.getEstimateSynergy() != 0) {
            float synergyHelper = task.getEstimateSynergy() * task.getSynergyBenefit().getSynergyBenefit();
            task.setSynergyTotal(synergyHelper);
        }

        task.setSpecificationTotal((float)task.getEstimateSpecification());
        if (task.getSpecificationFactor().getSpecificationFactor() != 0) {
            float specFactorHelper = task.getEstimateSpecification() * task.getSpecificationFactor().getSpecificationFactor();
            task.setSpecificationTotal(specFactorHelper);
        }

        task.setImplementationTotal((float)task.getEstimateImplementation());
        if (task.getImplementationFactor().getImplementationFactor() != 0) {
            float impFactorHelper = task.getEstimateImplementation() * task.getImplementationFactor().getImplementationFactor();
            task.setImplementationTotal(impFactorHelper);
        }

        task.setTestingTotal((float)task.getEstimateTesting());
        if(task.getTestingFactor().getTestingFactor() != 0){
            float testFactorHelper = task.getEstimateTesting() * task.getTestingFactor().getTestingFactor();
            task.setTestingTotal(testFactorHelper);
        }

        task.setEstimateTotal(task.getSpecificationTotal() + task.getImplementationTotal() + task.getTestingTotal());

        task.getOwnerRequirement().addTask(task);
        return task;
    }
}
