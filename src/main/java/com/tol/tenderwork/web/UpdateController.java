package com.tol.tenderwork.web;

import com.tol.tenderwork.domain.Project;
import com.tol.tenderwork.domain.Estimate;
import com.tol.tenderwork.domain.Requirement;
import com.tol.tenderwork.domain.Task;
import com.tol.tenderwork.repository.RequirementRepository;
import com.tol.tenderwork.repository.search.RequirementSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;


import javax.inject.Inject;


/**
 * Created by Sebastian Körkkö on 18.2.2016.
 * Edited by Petteri Salonurmi on 19.2.2016 11:26 GMT+2.
 */

@Repository
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

    public void updateRequirement(Requirement requirement){

        int specHelper = 0;
        log.debug("Spechelper", specHelper);
        log.debug("Task", requirement);
        for (Task task : requirement.getHasTaskss()) {
            specHelper = specHelper + task.getEstimateTotal();
            log.debug("IMP", task.getEstimateImplementation());
            log.debug("TOTAL", task.getEstimateTotal());
        }
        requirement.setTotalDuration(specHelper);

        Requirement result = requirementRepository.save(requirement);
        requirementSearchRepository.save(result);
    }

    public Task updateTask(Task task) {

        if(task.getSynergyCheck() == true) {
            float synergyHelper = task.getEstimateSynergy() * task.getSynergyBenefit().getSynergyBenefit();
            task.setSynergyTotal(Math.round(synergyHelper));
        }

        float specFactorHelper = (float)task.getEstimateSpecification() * task.getSpecificationFactor().getSpecificationFactor();
        float impFactorHelper = (float)task.getEstimateImplementation() * task.getImplementationFactor().getImplementationFactor();
        float testFactorHelper = (float)task.getEstimateTesting() * task.getTestingFactor().getTestingFactor();
        //task.setEstimateTotal((int) impFactorHelper + (int) testFactorHelper + (int) specFactorHelper);

        task.setSpecificationTotal(Math.round(specFactorHelper));
        task.setImplementationTotal(Math.round(impFactorHelper));
        task.setTestingTotal(Math.round(testFactorHelper));
        task.setEstimateTotal(Math.round((specFactorHelper + impFactorHelper + testFactorHelper)));
        return task;
    }

}
