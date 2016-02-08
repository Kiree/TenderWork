package com.tol.tenderwork.repository;

import com.tol.tenderwork.domain.Estimate;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Estimate entity.
 */
public interface EstimateRepository extends JpaRepository<Estimate,Long> {

}
