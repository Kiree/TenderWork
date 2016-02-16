package com.tol.tenderwork.repository;

import com.tol.tenderwork.domain.Project;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Project entity.
 */
public interface ProjectRepository extends JpaRepository<Project,Long> {

    @Query("select project from Project project where project.createdBy.login = ?#{principal.username}")
    List<Project> findByCreatedByIsCurrentUser();

    @Query("select project from Project project where project.editedBy.login = ?#{principal.username}")
    List<Project> findByEditedByIsCurrentUser();

}