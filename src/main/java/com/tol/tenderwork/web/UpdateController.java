package com.tol.tenderwork.web;

import com.tol.tenderwork.domain.Project;
import com.tol.tenderwork.domain.Estimate;
import com.tol.tenderwork.domain.Requirement;
import com.tol.tenderwork.domain.Task;


/**
 * Created by Sebastian Körkkö on 18.2.2016.
 */
/**
* Slack Integration Test
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
            task.setSynergyTotal((int) synergyHelper);
        }

        float impFactorHelper = (float)task.getEstimateImplementation() * task.getImplementationFactor().getImplementationFactor();
        float testFactorHelper = (float)task.getEstimateTesting() * task.getTestingFactor().getTestingFactor();
        float specFactorHelper = (float)task.getEstimateSpecification() * task.getSpecificationFactor().getSpecificationFactor();
        task.setEstimateTotal((int) impFactorHelper + (int) testFactorHelper + (int) specFactorHelper);
        return task;
    }

}
