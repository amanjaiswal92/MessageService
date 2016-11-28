package com.ail.narad.factory;

import com.ail.narad.domain.Templates;
import com.ail.narad.domain.enumeration.TemplateStatus;
import com.ail.narad.domain.enumeration.TemplateType;
import com.ail.narad.factory.messages.IMessageBean;
import com.ail.narad.factory.messages.PromotionalEmailMessage;
import com.ail.narad.messaging.consumers.PromotionalEmailConsumer;
import com.ail.narad.messagingimpl.RabbitMqClient;
import com.ail.narad.messagingimpl.RabbitMqUtils;
import com.ail.narad.models.MessageSenderModel;
import com.ail.narad.service.util.LogUtils;
import com.ail.narad.service.util.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class PromotionalEmail implements IMessageType {

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
			RabbitMqUtils.enqueueMsgs(messageBean, TemplateType.PROMOTIONAL_EMAIL.toString());
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception("Adding Promotional email to the queue failed");
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
		PromotionalEmailMessage message = new PromotionalEmailMessage();
		message.setBccAddresses(StringUtils.getListFromJsonArray(metaJson.getJSONArray("bcc")));
		message.setCcAddresses(StringUtils.getListFromJsonArray(metaJson.getJSONArray("cc")));
		message.setFromAddress(metaJson.getString("from"));
		message.setSubject(metaJson.getString("subject"));
		message.setMessage(templateMessage);
		message.setToAddresses(StringUtils.getListFromJsonArray(metaJson.getJSONArray("to")));
		message.setRequestId(LogUtils.getCurrentRequestId());
        message.setType(type);
		if(metaJson.has("delivery_time")) {
			message.setDeliveryDate(metaJson.getString("delivery_time"));
		}
		return message;
	}

	@Override
	public void consume(MessageSenderModel messageSenderModel) throws IOException {
		RabbitMqClient.consumeMsgs(new PromotionalEmailConsumer(messageSenderModel), TemplateType.PROMOTIONAL_EMAIL.toString());
	}


	@Override
	public String getRequestId(IMessageBean messageBean) {
		PromotionalEmailMessage message = (PromotionalEmailMessage)messageBean;
		return message.getRequestId();
	}

	@Override
	public IMessageBean getMessageBean(String type,Templates template, JSONObject dataJson, JSONObject metaJson, List<String> files) throws JSONException {
		String templateMessage = template.getMessage();
		List<String> variables = StringUtils.getListOfStringsInsideBracesWithBraces(templateMessage);
		for(int i = 0; i < variables.size(); i++) {
			String variableWithBraces = variables.get(i);
			String variable = variableWithBraces.substring(1, variableWithBraces.length() - 1);
			String value = dataJson.getString(variable);
			templateMessage = templateMessage.replace(variableWithBraces, value);
		}
		PromotionalEmailMessage message = new PromotionalEmailMessage();
		message.setBccAddresses(StringUtils.getListFromJsonArray(metaJson.getJSONArray("bcc")));
		message.setCcAddresses(StringUtils.getListFromJsonArray(metaJson.getJSONArray("cc")));
		message.setFromAddress(metaJson.getString("from"));
		message.setSubject(metaJson.getString("subject"));
		message.setMessage(templateMessage);
		message.setToAddresses(StringUtils.getListFromJsonArray(metaJson.getJSONArray("to")));
		message.setRequestId(LogUtils.getCurrentRequestId());
		message.setHasAttachment(true);
		message.setAttachmentFIleNames(files);
        message.setType(type);
		if(metaJson.has("delivery_time")) {
			message.setDeliveryDate(metaJson.getString("delivery_time"));
		}
		return message;
	}

}
