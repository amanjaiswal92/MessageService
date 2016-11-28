package com.ail.narad.factory.messagesenders;

import com.ail.narad.domain.enumeration.TemplateType;
import com.ail.narad.factory.messages.IMessageBean;

import java.util.List;

public interface IMessageSender {

	public void sendMessage(TemplateType templateType, IMessageBean messageBean);

	public void sendMessages(TemplateType templateType, List<IMessageBean> messageBeans);

}
