package com.ail.narad.factory.messagesenders;

import com.ail.narad.config.ApplicationProperties;
import com.ail.narad.domain.Audit_logs;
import com.ail.narad.domain.enumeration.AuditEvent;
import com.ail.narad.domain.enumeration.TemplateType;
import com.ail.narad.factory.AuditLogger;
import com.ail.narad.factory.MessagingFactory;
import com.ail.narad.factory.messages.IMessageBean;
import com.ail.narad.factory.messages.PromotionalEmailMessage;
import com.ail.narad.factory.messages.PromotionalSMSMessage;
import com.ail.narad.factory.messages.TransactionalSMSMessage;
import com.ail.narad.service.AddToLogService;
import com.ail.narad.service.util.HttpUtils;
import com.ail.narad.service.util.LogUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class KAPPSMSSender implements IMessageSender {

    @Autowired
    AddToLogService addToLogService;

	@Override
	public void sendMessage(TemplateType templateType, IMessageBean messageBean) {
		System.out.println("Reached KAPP SMS sender: "+ messageBean.serialize());

		String workingKey = ApplicationProperties.getCustomProperty("sms.kapp.working_key");
		String sender = ApplicationProperties.getCustomProperty("sms.kapp.sender");
		String baseUrl = ApplicationProperties.getCustomProperty("sms.kapp.base_url");

		String message = null;
		String requestId = null;
		String recipients = null;
		AuditLogger auditLogger = LogUtils.getAuditLogger();
		MessagingFactory messagingFactory = new MessagingFactory(StringUtils.lowerCase(templateType.toString()));

		if(messageBean != null) {
			if(messageBean instanceof TransactionalSMSMessage) {
				TransactionalSMSMessage transactionalSMSMessage = (TransactionalSMSMessage)messageBean;
				message = transactionalSMSMessage.getMessage();
				List<String> numberList = transactionalSMSMessage.getToNumbers();
				recipients = StringUtils.join(numberList, ',');
				requestId = transactionalSMSMessage.getRequestId();
			} else if(messageBean instanceof PromotionalEmailMessage) {
				PromotionalSMSMessage promotionalSMSMessage = (PromotionalSMSMessage)messageBean;
				message = promotionalSMSMessage.getMessage();
				List<String> numberList = promotionalSMSMessage.getToNumbers();
				recipients = StringUtils.join(numberList, ',');
				requestId = promotionalSMSMessage.getRequestId();
			}
		}

		Map<String, String> parameterMap = new HashMap<String, String>();
		try {
			parameterMap.put("message", URLEncoder.encode(message,"UTF-8"));
			parameterMap.put("to", recipients);
			parameterMap.put("sender", sender);
			parameterMap.put("workingkey", workingKey);

			String response = HttpUtils.doGetRequest(baseUrl, parameterMap, null);
            addToLogService.addToAuditTable(new Audit_logs(messagingFactory.getMessage().getRequestId(messageBean), AuditEvent.SENT_TO_SERVICE_PROVIDER, response));
			System.out.println("Response of KAPP SMS sender: "+ response);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
            addToLogService.addToAuditTable(new Audit_logs(messagingFactory.getMessage().getRequestId(messageBean), AuditEvent.FAILED, "Failed at service provider: "+e.getMessage()));
		}
	}

	@Override
	public void sendMessages(TemplateType templateType, List<IMessageBean> messageBeans) {
		for(int i = 0; i < messageBeans.size(); i++) {
			sendMessage(templateType, messageBeans.get(i));
		}
	}

}
