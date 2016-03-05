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
 * Created by PeHeri on 3.3.2016.
 */

/**
 * A Controller for handling different entities' calculations
 */
@Service
@Transactional
public class MathController {

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

    // Methods that calculate changes to entities //

    @Transactional
    public Estimate calculateEstimate(Estimate estimateHelper){
        Estimate estimate = estimateRepository.findOne(estimateHelper.getId());

        estimate.setSpecificationFactor(estimateHelper.getSpecificationFactor());
        estimate.setImplementationFactor(estimateHelper.getImplementationFactor());
        estimate.setTestingFactor(estimateHelper.getTestingFactor());
        estimate.setSynergyBenefit(estimateHelper.getSynergyBenefit());

        if (estimate.getHasRequirementss().isEmpty() != true) {
            Set<Requirement> requirements = estimate.getHasRequirementss();
            float totalDurationHelper = 0;
            float totalSynergyHelper = 0;

            for (Requirement r : requirements) {
                if (r.getTotalDuration() != null) {
                    totalDurationHelper = totalDurationHelper + r.getTotalDuration();
                }
                if (r.getSynergyBenefit() != null) {
                    totalSynergyHelper = totalSynergyHelper + r.getSynergyBenefit();
                }
            }

            if (totalSynergyHelper > 0 || estimate.getSynergyBenefit() > 0) {
                estimate.setTotalPrice((long) Math.round((totalDurationHelper - totalSynergyHelper) * estimate.getDailyPrice()));
                estimate.setTotalDuration(totalDurationHelper - totalSynergyHelper);
                estimate.setResourcing(estimate.getTotalDuration() / (estimate.getWorkdaysInMonth() * estimate.getWorkdaysInMonth()));
                estimate.setTotalSynergyBenefit(totalSynergyHelper);
            } else {
                estimate.setTotalPrice((long) totalDurationHelper * estimate.getDailyPrice());
                estimate.setResourcing(totalDurationHelper / (estimate.getWorkdaysInMonth() * estimate.getDesiredProjectDuration()));
                estimate.setTotalDuration(totalDurationHelper);
                estimate.setTotalSynergyBenefit((float) 0);
            }
        }
        return estimate;
    }

    @Transactional
    public Requirement calculateRequirement(Task task){
        Requirement requirement = requirementRepository.findOne(task.getOwnerRequirement().getId());
        Set<Task> tasks = requirement.getHasTaskss();
        //log.debug("Laskussa setti tehty");
        float totalDurationHelper = 0;
        float totalSpecificationHelper = 0;
        float totalImplementationHelper = 0;
        float totalTestingHelper = 0;
        float totalSynergyHelper = 0;

        if(requirement.getHasTaskss().isEmpty()) {
            //log.debug("Requirementilla ei taskeja: {}", requirement);
            return requirement;
        } else {
            //log.debug("Laskussa taski loop alkaa");
            for (Task t : tasks) {
                totalDurationHelper = totalDurationHelper + t.getEstimateTotal();
                totalSpecificationHelper = totalSpecificationHelper + t.getSpecificationTotal();
                totalImplementationHelper = totalImplementationHelper + t.getImplementationTotal();
                totalTestingHelper = totalTestingHelper + t.getTestingTotal();
                totalSynergyHelper = totalSynergyHelper + t.getSynergyTotal();
            }

            //log.debug("Laskussa setteri kutsut");
            requirement.setTotalDuration(totalDurationHelper);
            requirement.setDurationSpecification(totalSpecificationHelper);
            requirement.setDurationImplementation(totalImplementationHelper);
            requirement.setDurationTesting(totalTestingHelper);
            requirement.setSynergyBenefit(totalSynergyHelper);

            return requirement;
        }
    }

    @Transactional
    public Task calculateTask(Task task, Estimate estimate) {

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
}
