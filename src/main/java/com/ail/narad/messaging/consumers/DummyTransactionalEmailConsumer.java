package com.ail.narad.messaging.consumers;

import com.ail.narad.domain.Audit_logs;
import com.ail.narad.domain.enumeration.AuditEvent;
import com.ail.narad.domain.enumeration.TemplateType;
import com.ail.narad.factory.AuditLogger;
import com.ail.narad.factory.messages.DummyTransactionalEmailMessage;
import com.ail.narad.models.MessageSenderModel;
import com.ail.narad.service.AddToLogService;
import com.ail.narad.service.util.LogUtils;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class DummyTransactionalEmailConsumer implements Consumer {


	private MessageSenderModel messageSenderModel;

    @Autowired
    AddToLogService addToLogService;
	public DummyTransactionalEmailConsumer(MessageSenderModel messageSenderModel) {
		this.messageSenderModel = messageSenderModel;
	}

	@Override
	public void handleConsumeOk(String consumerTag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleCancelOk(String consumerTag) {
		// TODO Auto-generated method stub
	}

	@Override
	public void handleCancel(String consumerTag) throws IOException {
		// TODO Auto-generated method stub
	}

	@Override
	public void handleDelivery(String arg0, Envelope env,
			BasicProperties arg2, byte[] body) throws IOException {
		AuditLogger auditLogger = LogUtils.getAuditLogger();
		String message = new String(body, "UTF-8");
		System.out.println(" [x] Received '" + env.getRoutingKey() + "':'" + message + "'");
		try {
	        DummyTransactionalEmailMessage messageBean = (DummyTransactionalEmailMessage) DummyTransactionalEmailMessage.deSerialize(message);
	        if(auditLogger != null) {
                addToLogService.addToAuditTable(new Audit_logs(messageBean.getRequestId(), AuditEvent.DEQUEUED_FROM_QUEUE, ""));
	        }
        this.messageSenderModel.sendMessage(messageBean.getRequestId(),TemplateType.DUMMY_TRANSACTIONAL_EMAIL, messageBean);
	        if(auditLogger != null) {
                addToLogService.addToAuditTable(new Audit_logs(messageBean.getRequestId(), AuditEvent.SENT_TO_SERVICE_PROVIDER, ""));
			}
		} catch(Exception e) {
            addToLogService.addToAuditTable(new Audit_logs(message, AuditEvent.FAILED, e.getMessage()));
			e.printStackTrace();
		}

	}

	@Override
	public void handleShutdownSignal(String consumerTag,
			ShutdownSignalException sig) {
		// TODO Auto-generated method stub
	}

	@Override
	public void handleRecoverOk(String consumerTag) {
		// TODO Auto-generated method stub
	}

}
