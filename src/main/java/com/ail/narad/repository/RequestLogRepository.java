package com.ail.narad.repository;

import com.ail.narad.domain.RequestLog;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the RequestLog entity.
 */
public interface RequestLogRepository extends JpaRepository<RequestLog,Long> {

}
