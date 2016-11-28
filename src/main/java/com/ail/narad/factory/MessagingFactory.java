package com.ail.narad.factory;

import com.ail.narad.domain.enumeration.TemplateType;

public class MessagingFactory {

	private TemplateType templateType;
	
	public MessagingFactory(String type) {
		switch (type) {
		case "transactional_email":
			templateType = TemplateType.TRANSACTIONAL_EMAIL;
			break;
		case "transactional_sms":
			templateType = TemplateType.TRANSACTIONAL_SMS;
			break;
		case "promotional_email":
			templateType = TemplateType.PROMOTIONAL_EMAIL;
			break;
		case "promotional_sms":
			templateType = TemplateType.PROMOTIONAL_SMS;
			break;
		case "dummy_transactional_email":
			templateType = TemplateType.DUMMY_TRANSACTIONAL_EMAIL;
			break;
		case "ivr_call":
			templateType = TemplateType.IVR_CALL;
			break;	
		default:
			templateType = TemplateType.UNKNOWN;
			break;
		}
	}

	public TemplateType getTemplateType() {
		return templateType;
	}

	
	public IMessageType getMessage() {
		if(this.templateType == null)  {
			return null;
		}
		switch (this.templateType) {
		case TRANSACTIONAL_EMAIL:
			return new TransactionalEmail();
		case TRANSACTIONAL_SMS:
			return new TransactionalSMS();
		case PROMOTIONAL_EMAIL:
			return new PromotionalEmail();
		case PROMOTIONAL_SMS:
			return new PromotionalSMS();
		case DUMMY_TRANSACTIONAL_EMAIL:
			return new DummyTransactionalEmail();
		default:
			return null;
		}
	}
	
}
