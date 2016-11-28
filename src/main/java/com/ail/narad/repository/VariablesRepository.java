package com.ail.narad.repository;

import com.ail.narad.domain.Variables;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Variables entity.
 */
public interface VariablesRepository extends JpaRepository<Variables,Long> {

}
