package com.tol.tenderwork.web;

import com.tol.tenderwork.domain.Project;
import com.tol.tenderwork.domain.Estimate;
import com.tol.tenderwork.domain.Requirement;
import com.tol.tenderwork.domain.Task;
import com.tol.tenderwork.repository.EstimateRepository;
import com.tol.tenderwork.repository.RequirementRepository;
import com.tol.tenderwork.repository.search.EstimateSearchRepository;
import com.tol.tenderwork.repository.search.RequirementSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.inject.Inject;
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

    public Project updateProject(Project project){

        return project;
    }

    public Estimate updateEstimate(Estimate estimate){


        return estimate;
    }

    @Transactional
    public void updateEstimate(Requirement requirement){
        Estimate estimate = estimateRepository.findOne(requirement.getOwnerEstimate().getId());
        Set<Requirement> requirements = estimate.getHasRequirementss();
        float totalDurationHelper = 0;

        log.debug("REQS: ", requirements);

        for(Requirement r : requirements) {
            totalDurationHelper = totalDurationHelper + r.getTotalDuration();
        }
        totalDurationHelper  = Math.round(totalDurationHelper);
        estimate.setTotalDuration(totalDurationHelper);
        estimate.setTotalPrice((int)totalDurationHelper * estimate.getDailyPrice());
        estimate.setResourcing(totalDurationHelper / (estimate.getWorkdaysInMonth() * estimate.getDesiredProjectDuration()));

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

        requirement.getOwnerEstimate().addRequirement(requirement);
    }

    @Transactional
    public Task updateTask(Task task) {
        if(task.getSynergyCheck() == true) {
            float synergyHelper = task.getEstimateSynergy() * task.getSynergyBenefit().getSynergyBenefit();
            task.setSynergyTotal(synergyHelper);
        }

        float specFactorHelper = task.getEstimateSpecification() * task.getSpecificationFactor().getSpecificationFactor();
        float impFactorHelper = task.getEstimateImplementation() * task.getImplementationFactor().getImplementationFactor();
        float testFactorHelper = task.getEstimateTesting() * task.getTestingFactor().getTestingFactor();

        task.setSpecificationTotal(specFactorHelper);
        task.setImplementationTotal(impFactorHelper);
        task.setTestingTotal(testFactorHelper);
        task.setEstimateTotal(specFactorHelper + impFactorHelper + testFactorHelper);

        task.getOwnerRequirement().addTask(task);
        return task;
    }
}
