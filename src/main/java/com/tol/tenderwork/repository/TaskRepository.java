package com.tol.tenderwork.repository;

import com.tol.tenderwork.domain.Task;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Task entity.
 */
public interface TaskRepository extends JpaRepository<Task,Long> {

    @Query("select task from Task task where task.ownedBy.login = ?#{principal.username}")
    List<Task> findByOwnedByIsCurrentUser();

}
