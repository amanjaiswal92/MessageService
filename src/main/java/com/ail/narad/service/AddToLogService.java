package com.ail.narad.service;

import com.ail.narad.domain.Audit_logs;

public interface AddToLogService{

	public void addToRequestTable(String requestId, String type, String requestBody);
	public void addToAuditTable(Audit_logs auditLogsBean);

}
