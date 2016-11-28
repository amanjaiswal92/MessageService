package com.ail.narad.factory;

import com.ail.narad.domain.Templates;
import com.ail.narad.factory.messages.IMessageBean;
import com.ail.narad.models.MessageSenderModel;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public interface IMessageType {

	public Boolean isMessageValid(Templates template, JSONObject data);

	public IMessageBean getMessageBean(String type,Templates template, JSONObject dataJson, JSONObject metaJson) throws JSONException;

	public IMessageBean getMessageBean(String type,Templates template, JSONObject dataJson, JSONObject metaJson, List<String> files) throws JSONException;

	public Boolean addToQueue(IMessageBean messageBean) throws Exception;

	public void consume(MessageSenderModel messageSenderModel)  throws IOException;

	public String getRequestId(IMessageBean messageBean);

}
