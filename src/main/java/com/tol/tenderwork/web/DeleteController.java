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
import org.springframework.beans.factory.annotation.Autowired;
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
 * A controller for handling delete operations.
 */
@Service
@Transactional
public class DeleteController {

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
    private UpdateController updateController;

    @Autowired
    private MathController mathController;

    @Autowired
    private SaveController saveController;

    @Transactional
    public void deleteTask(Long id){

        // Call to update owner project's edit information
        updateController.updateProject(taskRepository.findOne(id).getOwnerRequirement().getOwnerEstimate().getOwnerProject(),
            taskRepository.findOne(id).getOwnedBy());

        taskRepository.delete(id);
        taskSearchRepository.delete(id);
    }

    @Transactional
    public void deleteEstimate(Long id){

        // Call to update owner project's edit information
        updateController.updateProject(estimateRepository.findOne(id).getOwnerProject(),
            estimateRepository.findOne(id).getCreatedBy());

        estimateRepository.delete(id);
        estimateSearchRepository.delete(id);
    }

    @Transactional
    public void deleteRequirement(Long id){

        // Call to update owner project's edit information
        updateController.updateProject(requirementRepository.findOne(id).getOwnerEstimate().getOwnerProject(),
            requirementRepository.findOne(id).getOwner());

        requirementRepository.delete(id);
        requirementSearchRepository.delete(id);
    }

    @Transactional
    public void deleteProject(Long id){

        projectRepository.delete(id);
        projectSearchRepository.delete(id);
    }
}
