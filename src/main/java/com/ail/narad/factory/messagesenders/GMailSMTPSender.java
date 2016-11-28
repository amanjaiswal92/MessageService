package com.ail.narad.factory.messagesenders;

import com.ail.narad.domain.enumeration.TemplateType;
import com.ail.narad.factory.messages.IMessageBean;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GMailSMTPSender implements IMessageSender {

	@Override
	public void sendMessage(TemplateType templateType, IMessageBean messageBean) {
		System.out.println("Reached GMail SMTP sender: "+ messageBean.serialize());
	}

	@Override
	public void sendMessages(TemplateType templateType, List<IMessageBean> messageBeans) {
		for(int i = 0; i < messageBeans.size(); i++) {
			sendMessage(templateType, messageBeans.get(i));
		}
	}

}
