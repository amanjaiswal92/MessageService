package com.ail.narad.repository;

import com.ail.narad.domain.CallDetails;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the CallDetails entity.
 */
public interface CallDetailsRepository extends JpaRepository<CallDetails,Long> {

}
