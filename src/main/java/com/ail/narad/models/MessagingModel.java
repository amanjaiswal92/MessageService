package com.ail.narad.models;

import com.ail.narad.domain.Audit_logs;
import com.ail.narad.domain.Templates;
import com.ail.narad.domain.enumeration.AuditEvent;
import com.ail.narad.factory.AuditLogger;
import com.ail.narad.factory.MessagingFactory;
import com.ail.narad.factory.messages.IMessageBean;
import com.ail.narad.service.AddToLogService;
import com.ail.narad.service.util.LogUtils;
import com.ailiens.common.RabbitMqMessageSender;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.ail.narad.web.rest.KeycloakService.log;

@Service
public class MessagingModel {

	@Autowired
    RabbitMqMessageSender rabbitMqMessageSender;

    @Autowired
    AddToLogService addToLogService;

    public void addToQueue(String requestId, Templates template, String type, JSONObject dataJson, JSONObject metaJson, List<String> files) throws Exception {

		AuditLogger auditLogger = LogUtils.getAuditLogger();
		MessagingFactory messagingFactory = new MessagingFactory(type);
        if(messagingFactory.getMessage().isMessageValid(template, dataJson)) {
        	IMessageBean messageBean;
        	if(files != null && !files.isEmpty()) {
	        	messageBean = messagingFactory.getMessage().getMessageBean(type,template, dataJson, metaJson, files);
        	} else {
        		messageBean = messagingFactory.getMessage().getMessageBean(type,template, dataJson, metaJson);
        	}
        	try{
                rabbitMqMessageSender.sendMessage(messageBean,"naradNew");
            }catch (Exception e){
                log.error("Message is not added in queue: " + e.getMessage());
                throw new Exception ("Message is not added in queue: " + e.getMessage());
            }
            if(auditLogger != null) {
                addToLogService.addToAuditTable(new Audit_logs(requestId,AuditEvent.ADDED_TO_QUEUE, ""));
        	}
        } else {
        	if(auditLogger != null) {
                log.error("Message is not valid and not added to the queue");
        		auditLogger.addToQueue(new Audit_logs(requestId,AuditEvent.FAILED, "Message is not valid and not added to the queue "));
        	}
        	throw new Exception("Message data and template data doesnt match");
        }
	}

}
