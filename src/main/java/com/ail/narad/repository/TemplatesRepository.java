package com.ail.narad.repository;

import com.ail.narad.domain.Templates;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Templates entity.
 */
public interface TemplatesRepository extends JpaRepository<Templates,Long> {

}
