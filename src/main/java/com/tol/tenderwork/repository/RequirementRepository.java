package com.tol.tenderwork.repository;

import com.tol.tenderwork.domain.Requirement;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Requirement entity.
 */
public interface RequirementRepository extends JpaRepository<Requirement,Long> {

    @Query("select requirement from Requirement requirement where requirement.owner.login = ?#{principal.username}")
    List<Requirement> findByOwnerIsCurrentUser();

    @Query("select distinct requirement from Requirement requirement left join fetch requirement.tags")
    List<Requirement> findAllWithEagerRelationships();

    @Query("select requirement from Requirement requirement left join fetch requirement.tags where requirement.id =:id")
    Requirement findOneWithEagerRelationships(@Param("id") Long id);

}
