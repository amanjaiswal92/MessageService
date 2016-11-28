package com.ail.narad.repository;

import com.ail.narad.domain.CallRequest;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the CallRequest entity.
 */
public interface CallRequestRepository extends JpaRepository<CallRequest,Long> {

}
