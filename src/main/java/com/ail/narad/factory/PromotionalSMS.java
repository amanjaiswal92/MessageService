package com.ail.narad.factory;

import com.ail.narad.domain.Templates;
import com.ail.narad.domain.enumeration.TemplateStatus;
import com.ail.narad.domain.enumeration.TemplateType;
import com.ail.narad.factory.messages.IMessageBean;
import com.ail.narad.factory.messages.PromotionalSMSMessage;
import com.ail.narad.messaging.consumers.PromotionalSMSConsumer;
import com.ail.narad.messagingimpl.RabbitMqClient;
import com.ail.narad.messagingimpl.RabbitMqUtils;
import com.ail.narad.models.MessageSenderModel;
import com.ail.narad.service.util.LogUtils;
import com.ail.narad.service.util.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class PromotionalSMS implements IMessageType {

	@Override
	public Boolean isMessageValid(Templates template, JSONObject dataJson) {
		if(template.getStatus() != TemplateStatus.APPROVED) {
			return false;
		}
		String templateMessage = template.getMessage();
		List<String> variables = StringUtils.getListOfStringsInsideBracesWithBraces(templateMessage);
		if((dataJson == null || dataJson.length() == 0) && (variables == null || variables.isEmpty())) {
			return true;
		}
		for(int i = 0; i < variables.size(); i++) {
			String variableWithBraces = variables.get(i);
			String variable = variableWithBraces.substring(1, variableWithBraces.length() - 1);
			if(!dataJson.has(variable)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public Boolean addToQueue(IMessageBean messageBean) throws Exception {
		try {
			RabbitMqUtils.enqueueMsgs(messageBean, TemplateType.PROMOTIONAL_SMS.toString());
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception("Adding Promotional sms to the queue failed");
		}
	}

	@Override
	public IMessageBean getMessageBean(String type,Templates template, JSONObject dataJson, JSONObject metaJson) throws JSONException {
		String templateMessage = template.getMessage();
		List<String> variables = StringUtils.getListOfStringsInsideBracesWithBraces(templateMessage);
		for(int i = 0; i < variables.size(); i++) {
			String variableWithBraces = variables.get(i);
			String variable = variableWithBraces.substring(1, variableWithBraces.length() - 1);
			String value = dataJson.getString(variable);
			templateMessage = templateMessage.replace(variableWithBraces, value);
		}
		PromotionalSMSMessage message = new PromotionalSMSMessage();
		message.setToNumbers(StringUtils.getListFromJsonArray(metaJson.getJSONArray("to")));
		message.setMessage(templateMessage);
		message.setRequestId(LogUtils.getCurrentRequestId());
        message.setType(type);
        return message;

	}

	@Override
	public IMessageBean getMessageBean(String type,Templates template, JSONObject dataJson, JSONObject metaJson, List<String> files) throws JSONException {
		return null;
	}

	@Override
	public void consume(MessageSenderModel messageSenderModel) throws IOException {
		RabbitMqClient.consumeMsgs(new PromotionalSMSConsumer(messageSenderModel), TemplateType.PROMOTIONAL_SMS.toString());
	}


	@Override
	public String getRequestId(IMessageBean messageBean) {
		PromotionalSMSMessage message = (PromotionalSMSMessage)messageBean;
		return message.getRequestId();
	}
}
