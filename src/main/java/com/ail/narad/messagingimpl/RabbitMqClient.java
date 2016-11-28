package com.ail.narad.messagingimpl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.springframework.stereotype.Component;

import com.ail.narad.config.ApplicationProperties;
import com.ail.narad.service.util.LogUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;

@Component
public class RabbitMqClient {

	static LogUtils logger = new LogUtils(RabbitMqClient.class);

	private static Connection connection = null;

	private static Channel msgExgchannel;

	private static Channel loggingChannel;

	private static Channel IvrChannel;

	public static final String msgExghannelName = "msg_exchange";

	public static final String logChannelName = "log";

	public static final String IvrExchangeName = "IVR_calls";

	public static Map<String, Boolean> bindMsgExgMap;

	public static Map<String, Boolean> bindLogMap;

	public static Map<String, Boolean> bindIvrMap;

	public static Map<Class, Boolean> consumerMsgsMap;

	public static Map<Class, Boolean> consumerLogsMap;

	public static Map<Class, Boolean> consumerIvrMap;
	
/*	@Inject
	private static ApplicationProperties applicationProperties;*/

	
	/*RabbitMqClient() { getConnection(); }*/
	 

	public static Connection getConnection() {
		if (connection == null) {
			try {
				ConnectionFactory factory = new ConnectionFactory();
			//	factory.setHost("52.91.175.134");
				factory.setHost(ApplicationProperties.getCustomProperty("rabbitmq.host"));
				
			//	factory.setUsername("guest");
				factory.setPort(5672);
				
				factory.setUsername(ApplicationProperties.getCustomProperty("rabbitmq.username"));
			//	factory.setPassword("guest");
				factory.setPassword(ApplicationProperties.getCustomProperty("rabbitmq.password"));
				factory.setAutomaticRecoveryEnabled(true);
			
				connection = factory.newConnection();
				logger.info("Initialized connection to RabbitMq");

				bindMsgExgMap = new HashMap<String, Boolean>();
				bindIvrMap = new HashMap<String, Boolean>();
				bindLogMap = new HashMap<String, Boolean>();
				consumerMsgsMap = new HashMap<Class, Boolean>();
				consumerLogsMap = new HashMap<Class, Boolean>();
				consumerIvrMap = new HashMap<Class, Boolean>();
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Problem instantiating the RabbitMq client");
			}
		}
		return connection;
	}

	public static void bindIVRExgChannel(String routingKey) throws IOException {
		if (bindIvrMap == null || bindIvrMap.isEmpty() || !bindIvrMap.containsKey(routingKey)
				|| (bindIvrMap.containsKey(routingKey) && !bindIvrMap.get(routingKey))) {
			Channel channel = getIVRChannel();
			String queueName = channel.queueDeclare(routingKey, true, false, false, null).getQueue();
			channel.queueBind(queueName, IvrExchangeName, routingKey);
			bindIvrMap.put(routingKey, true);
		}
	}

	public static void bindMsgExgChannel(String routingKey) throws IOException {
		if (bindMsgExgMap == null || bindMsgExgMap.isEmpty() || !bindMsgExgMap.containsKey(routingKey)
				|| (bindMsgExgMap.containsKey(routingKey) && !bindMsgExgMap.get(routingKey))) {
			Channel channel = getMsgExgChannel();
			String queueName = channel.queueDeclare(routingKey, true, false, false, null).getQueue();
			channel.queueBind(queueName, msgExghannelName, routingKey);
			bindMsgExgMap.put(routingKey, true);
		}
	}

	public static void bindLogChannel(String routingKey) throws IOException {
		if (bindLogMap == null || bindLogMap.isEmpty() || !bindLogMap.containsKey(routingKey)
				|| (bindLogMap.containsKey(routingKey) && !bindLogMap.get(routingKey))) {
			Channel channel = getLoggingChannel();
			String queueName = channel.queueDeclare(routingKey, true, false, false, null).getQueue();
			channel.queueBind(queueName, logChannelName, routingKey);
			bindLogMap.put(routingKey, true);
		}
	}

	public static void consumeIvrCalls(Consumer consumer, String queueName) throws IOException {
		// TODO : Do the same split for other if conditions
		// The null and isEmpty are thing an exception to rabbitMQ
		if (consumerIvrMap == null || consumerIvrMap.isEmpty() || !consumerIvrMap.containsKey(consumer.getClass())
				|| (consumerIvrMap.containsKey(consumer.getClass()) && !consumerIvrMap.get(consumer.getClass()))) {
			Channel channel = getIVRChannel();
			// String queueName = channel.queueDeclare().getQueue();
			channel.basicConsume(queueName, true, consumer);
			consumerIvrMap.put(consumer.getClass(), true);
		}
	}

	public static void consumeMsgs(Consumer consumer, String queueName) throws IOException {
		// TODO : Do the same split for other if conditions
		// The null and isEmpty are thing an exception to rabbitMQ
		if (consumerMsgsMap == null || consumerMsgsMap.isEmpty() || !consumerMsgsMap.containsKey(consumer.getClass())
				|| (consumerMsgsMap.containsKey(consumer.getClass()) && !consumerMsgsMap.get(consumer.getClass()))) {
			Channel channel = getMsgExgChannel();
			String queueName1 = channel.queueDeclare().getQueue();
			channel.basicConsume(queueName, true, consumer);
			consumerMsgsMap.put(consumer.getClass(), true);
		}
	}

	public static void consumeLogs(Consumer consumer, String queueName) throws IOException {
		if (consumerLogsMap == null || consumerLogsMap.isEmpty() || !consumerLogsMap.containsKey(consumer.getClass())
				|| (consumerLogsMap.containsKey(consumer.getClass()) && !consumerLogsMap.get(consumer.getClass()))) {
			Channel channel = getLoggingChannel();
//			String queueName1 = channel.queueDeclare().getQueue();
			channel.basicConsume(queueName, true, consumer);
			consumerLogsMap.put(consumer.getClass(), true);
		}
	}

	public static Boolean disconnectConnection() {
		if (connection != null) {
			try {
				msgExgchannel.close();
				loggingChannel.close();
				connection.close();
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("Cannot close connecton");
				return false;
			} catch (TimeoutException e) {
				e.printStackTrace();
				logger.error("Timeout while closing the channel");
			}
			;
			return true;
		}
		return false;
	}

	public static Channel getMsgExgChannel() {
		if (connection == null) {
			try {
				msgExgchannel = getConnection().createChannel();
				msgExgchannel.exchangeDeclare(msgExghannelName, "direct");
				return msgExgchannel;
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("Channel cannot be created");
				return null;
			}
		}
		if (msgExgchannel != null) {
			return msgExgchannel;
		}
		try {
			msgExgchannel = connection.createChannel();
			msgExgchannel.exchangeDeclare(msgExghannelName, "direct");
			return msgExgchannel;
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Channel cannot be created");
			return null;
		}
	}

	public static Channel getIVRChannel() {
		if (connection == null) {

			try {
				IvrChannel = getConnection().createChannel();
				Map<String, Object> args = new HashMap<String, Object>();
				  args.put("x-delayed-type", "direct");
				IvrChannel.exchangeDeclare(IvrExchangeName, "x-delayed-message", true,false, args);
				return IvrChannel;
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("Channel cannot be created -------------------------------");
				return null;
			}

		}
		if (IvrChannel != null) {
			return IvrChannel;
		}
		try {
			IvrChannel = connection.createChannel();
			IvrChannel.exchangeDeclare(IvrExchangeName, "direct");
			return IvrChannel;
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Channel cannot be created");
			return null;
		}

	}

	public static Channel getLoggingChannel() {
		if (connection == null) {
			return null;
		}
		if (loggingChannel != null) {
			return loggingChannel;
		}
		try {
			loggingChannel = connection.createChannel();
			loggingChannel.exchangeDeclare(logChannelName, "direct");
			return loggingChannel;
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Channel cannot be created");
			return null;
		}
	}
}
