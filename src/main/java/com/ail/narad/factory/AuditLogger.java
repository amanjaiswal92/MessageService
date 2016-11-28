package com.ail.narad.factory;

import java.io.IOException;

import com.ail.narad.domain.Audit_logs;
import com.ail.narad.messaging.consumers.AuditLogConsumer;
import com.ail.narad.messagingimpl.RabbitMqClient;
import com.ail.narad.messagingimpl.RabbitMqUtils;
import com.ail.narad.models.AuditLogModel;
import com.ail.narad.service.util.LogUtils;

public class AuditLogger {
	
	LogUtils logger = new LogUtils(AuditLogger.class);
	
	private AuditLogModel auditLogModel;
	
	public AuditLogger(AuditLogModel auditLogModel) {
		this.auditLogModel = auditLogModel;
	}
	
	public Boolean addToQueue(Audit_logs auditLogsBean) {
		try {
			RabbitMqUtils.enqueueLogs(auditLogsBean, "audit_log");
			consume(this.auditLogModel);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Adding Audit Log to the queue failed");
			return false;
		}
	}
	
	public void consume(AuditLogModel auditLogModel) throws IOException {
		RabbitMqClient.consumeLogs(new AuditLogConsumer(auditLogModel), "audit_log");
	}
}
