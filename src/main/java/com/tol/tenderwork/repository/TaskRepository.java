package com.tol.tenderwork.repository;

import com.tol.tenderwork.domain.Task;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Task entity.
 */
public interface TaskRepository extends JpaRepository<Task,Long> {

    @Query("select task from Task task where task.ownedBy.login = ?#{principal.username}")
    List<Task> findByOwnedByIsCurrentUser();

    @Query("select distinct task from Task task left join fetch task.tags")
    List<Task> findAllWithEagerRelationships();

    @Query("select task from Task task left join fetch task.hasTagss where task.id =:id")
    Task findOneWithEagerRelationships(@Param("id") Long id);

}
