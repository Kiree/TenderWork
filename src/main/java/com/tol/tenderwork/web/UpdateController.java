package com.tol.tenderwork.web;

import com.tol.tenderwork.domain.Project;
import com.tol.tenderwork.domain.Estimate;
import com.tol.tenderwork.domain.Requirement;
import com.tol.tenderwork.domain.Task;
import com.tol.tenderwork.repository.RequirementRepository;
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

    public Project updateProject(Project project){

        return project;
    }

    public Estimate updateEstimate(Estimate estimate){

        return estimate;
    }

    @Transactional
    public void updateRequirement(Task task){
        Requirement requirement = requirementRepository.findOne(task.getOwnerRequirement().getId());
        Set<Task> tasks = requirement.getHasTaskss();
        float specHelper = 0;/*
        log.debug("Spechelper", specHelper);
        log.debug("Task", requirement);*/
        for (Task t : tasks) {
            specHelper = specHelper + t.getEstimateTotal();
            //log.debug("IMP", task.getEstimateImplementation());
            //log.debug("TOTAL", task.getEstimateTotal());
        }
        requirement.setTotalDuration(specHelper);

        Requirement result = requirementRepository.save(requirement);
        requirementSearchRepository.save(result);
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
        //task.setEstimateTotal((int) impFactorHelper + (int) testFactorHelper + (int) specFactorHelper);

        task.setSpecificationTotal(specFactorHelper);
        task.setImplementationTotal(impFactorHelper);
        task.setTestingTotal(testFactorHelper);
        task.setEstimateTotal(specFactorHelper + impFactorHelper + testFactorHelper);

        task.getOwnerRequirement().addTask(task);
        return task;
    }

}
