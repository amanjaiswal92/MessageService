package com.ail.narad.messagingimpl;

import java.io.IOException;

import com.ail.narad.factory.messages.IMessageBean;

public class RabbitMqUtils {
	
	public static void enqueueMsgs(IMessageBean messageBean, String bindingKey) throws IOException {
		RabbitMqClient.bindMsgExgChannel(bindingKey);
		RabbitMqClient.getMsgExgChannel().basicPublish(RabbitMqClient.msgExghannelName, bindingKey, null, messageBean.serialize().getBytes());
	}
	
	public static void enqueueLogs(IMessageBean messageBean, String bindingKey) throws IOException {
		RabbitMqClient.bindLogChannel(bindingKey);
		RabbitMqClient.getLoggingChannel().basicPublish(RabbitMqClient.logChannelName, bindingKey, null, messageBean.serialize().getBytes());
	}
	
	public static void enqueueCalls(IMessageBean messageBean, String bindingKey) throws IOException {
		RabbitMqClient.bindIVRExgChannel(bindingKey);
		RabbitMqClient.getIVRChannel().basicPublish(RabbitMqClient.IvrExchangeName, bindingKey, null, messageBean.serialize().getBytes());
	}
	
	public static void enqueueCalls1(String messageBean, String bindingKey) throws IOException {
		RabbitMqClient.bindIVRExgChannel(bindingKey);
		RabbitMqClient.getIVRChannel().basicPublish(RabbitMqClient.IvrExchangeName, bindingKey, null, messageBean.getBytes());
	}
}
