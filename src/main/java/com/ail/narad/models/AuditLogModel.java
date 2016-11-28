package com.ail.narad.models;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.ail.narad.domain.Audit_logs;
import com.ail.narad.repository.custom.CustomAuditLogRepository;
import com.ail.narad.service.util.LogUtils;

@Service
public class AuditLogModel {

	LogUtils logger = new LogUtils(AuditLogModel.class);
	
	@Inject
	private CustomAuditLogRepository customAuditLogRepository;
	
	public void saveToAudit(Audit_logs auditLogs) {
		customAuditLogRepository.save(auditLogs);
	}
	
}
