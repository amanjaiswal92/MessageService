package com.ail.narad.factory.messagesenders;

import java.util.List;

import com.ail.narad.domain.enumeration.TemplateType;
import com.ail.narad.factory.messages.IMessageBean;

public class IvrCallMaker implements IMessageSender{

	@Override
	public void sendMessage(TemplateType templateType, IMessageBean messageBean) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendMessages(TemplateType templateType, List<IMessageBean> messageBeans) {
		// TODO Auto-generated method stub
		
	}
	
	public void IvrCallMaker(String PhoneNO){
		
		System.out.println("-------------------------");
		System.out.println("phoneNO");
		
	}

}
