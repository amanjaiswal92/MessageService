package com.ail.narad.models;

import com.ail.narad.domain.Audit_logs;
import com.ail.narad.domain.Messagesenders;
import com.ail.narad.domain.enumeration.AuditEvent;
import com.ail.narad.domain.enumeration.Message_sender_status;
import com.ail.narad.domain.enumeration.TemplateType;
import com.ail.narad.factory.AuditLogger;
import com.ail.narad.factory.MessagingFactory;
import com.ail.narad.factory.messages.IMessageBean;
import com.ail.narad.factory.messagesenders.MessageSenderFactory;
import com.ail.narad.repository.custom.CustomMessagesendersRepository;
import com.ail.narad.service.AddToLogService;
import com.ail.narad.service.util.LogUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
public class MessageSenderModel {

	LogUtils logger = new LogUtils(MessageSenderModel.class);

    @Autowired
    AddToLogService addToLogService;

    @Autowired
    MessageSenderFactory messageSenderFactory;

	@Inject
	private CustomMessagesendersRepository customMessageSendersRepository;





	private Messagesenders getActiveMessageSenderByType(TemplateType templateType) {
		List<Messagesenders> messageSenders = customMessageSendersRepository.findMessageSendersByType(templateType, Message_sender_status.APPROVED);
		if(messageSenders == null || messageSenders.isEmpty())
			return null;
		//return the first approved one
		return messageSenders.get(0);
	}

	public void sendMessage(String requestId,TemplateType templateType, IMessageBean messageBean) {
		Messagesenders messageSender = getActiveMessageSenderByType(templateType);
		MessagingFactory messagingFactory = new MessagingFactory(StringUtils.lowerCase(templateType.toString()));
		AuditLogger auditLogger = LogUtils.getAuditLogger();
		if(messageSender == null) {
            logger.error("No Active message senders configured for message: " + messageBean.serialize());
            if (auditLogger != null) {
                addToLogService.addToAuditTable(new Audit_logs(messagingFactory.getMessage().getRequestId(messageBean), AuditEvent.FAILED, "No Active message senders configured for message"));
            }
            return;
        }

		String senderId = messageSender.getSender_id();
        messageSenderFactory.setMessageSenderId(senderId);
        messageSenderFactory.sendMessage(templateType, messageBean);
	}

}
