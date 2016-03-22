package com.tol.tenderwork.repository;

import com.tol.tenderwork.domain.Tag;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Tag entity.
 */
public interface TagRepository extends JpaRepository<Tag,Long> {

    @Query("select distinct tag from Tag tag left join fetch tag.belongsToProjectss")
    List<Tag> findAllWithEagerRelationships();

    @Query("select tag from Tag tag left join fetch tag.belongsToProjectss where tag.id =:id")
    Tag findOneWithEagerRelationships(@Param("id") Long id);

}
