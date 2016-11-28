package com.ail.narad.messaging.consumers;

import java.io.IOException;

import com.ail.narad.domain.Audit_logs;
import com.ail.narad.models.AuditLogModel;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;

public class AuditLogConsumer implements Consumer {
	
	private AuditLogModel auditLogModel;
	
	public AuditLogConsumer(AuditLogModel auditLogsModel) {
		this.auditLogModel = auditLogsModel;
	}
	
	@Override
	public void handleConsumeOk(String consumerTag) {
		
	}

	@Override
	public void handleCancelOk(String consumerTag) {
		
	}

	@Override
	public void handleCancel(String consumerTag) throws IOException {
		
	}

	@Override
	public void handleDelivery(String arg0, Envelope env,
			BasicProperties arg2, byte[] body) throws IOException {
		
		String message = new String(body, "UTF-8");
        System.out.println(" [x] Received '" + env.getRoutingKey() + "':'" + message + "'");
        Audit_logs auditBean = (Audit_logs) Audit_logs.deSerialize(message);
        if(this.auditLogModel != null) {
        	this.auditLogModel.saveToAudit(auditBean);
        }
	}

	@Override
	public void handleShutdownSignal(String consumerTag,
			ShutdownSignalException sig) {
		
	}

	@Override
	public void handleRecoverOk(String consumerTag) {
		
	}

}
