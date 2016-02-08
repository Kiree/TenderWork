package com.tol.tenderwork.repository;

import com.tol.tenderwork.domain.Testity;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Testity entity.
 */
public interface TestityRepository extends JpaRepository<Testity,Long> {

}
