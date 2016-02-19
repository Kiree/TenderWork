package com.tol.tenderwork.web;

import com.tol.tenderwork.domain.Project;
import com.tol.tenderwork.domain.Estimate;
import com.tol.tenderwork.domain.Requirement;
import com.tol.tenderwork.domain.Task;


/**
 * Created by Sebastian Körkkö on 18.2.2016.
 * Edited by Petteri Salonurmi on 19.2.2016 11:26 GMT+2.
 */

public class UpdateController {

    public Project updateProject(Project project){

        return project;
    }

    public Estimate updateEstimate(Estimate estimate){

        return estimate;
    }

    public Requirement updateRequirement(Requirement requirement){

        return requirement;
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
