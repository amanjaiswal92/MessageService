package com.ail.narad.factory.messagesenders;

import com.ail.narad.domain.enumeration.TemplateType;
import com.ail.narad.factory.messages.IMessageBean;
import com.ail.narad.service.util.LogUtils;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class MessageSenderFactory {

	private LogUtils logger = new LogUtils(MessageSenderFactory.class);

	@Setter
    private String messageSenderId;



    @Autowired
    List<IMessageSender> messageSenderList;

    @Autowired
    MailgunAPISender mailgunAPISender;

    @Autowired
    KAPPSMSSender kappsmsSender;

    @Autowired
    GMailSMTPSender gMailSMTPSender;

    @Autowired
    DummyAPISender dummyAPISender;

	public void sendMessage(TemplateType templateType, IMessageBean messageBean) {
		switch (this.messageSenderId) {
		case "KAPP":
			kappsmsSender.sendMessage(templateType, messageBean);
			break;
		case "GMAIL_SMTP":
			gMailSMTPSender.sendMessage(templateType, messageBean);
			break;
		case "MAILGUN":
			mailgunAPISender.sendMessage(templateType, messageBean);
			break;
		case "DUMMY":
			dummyAPISender.sendMessage(templateType, messageBean);
		default:
			logger.error("No Message sender configured for message bean: "+messageBean.serialize());
			break;
		}
	}

}
