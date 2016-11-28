package com.ail.narad.repository;

import com.ail.narad.domain.Audit_logs;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Audit_logs entity.
 */
public interface Audit_logsRepository extends JpaRepository<Audit_logs,Long> {

}
