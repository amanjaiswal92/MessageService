package com.ail.narad.factory;

import com.ail.narad.domain.Templates;
import com.ail.narad.domain.enumeration.TemplateType;
import com.ail.narad.factory.messages.IMessageBean;
import com.ail.narad.messaging.consumers.IvrCallsConsumer;
import com.ail.narad.messagingimpl.RabbitMqClient;
import com.ail.narad.messagingimpl.RabbitMqUtils;
import com.ail.narad.models.MessageSenderModel;
import com.ail.narad.service.IvrCallService;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;


@Component
public class IvrCalls implements IMessageType {

	static boolean autoAck = false;

	@Autowired
	private IvrCallService ivrCallService;

	@Override
	public Boolean isMessageValid(Templates template, JSONObject data) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public IMessageBean getMessageBean(String type,Templates template, JSONObject dataJson, JSONObject metaJson)
			throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IMessageBean getMessageBean(String type,Templates template, JSONObject dataJson, JSONObject metaJson, List<String> files)
			throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean addToQueue(IMessageBean messageBean) throws Exception {
		// TODO Auto-generated method stub

		try {
			RabbitMqUtils.enqueueCalls(messageBean, TemplateType.IVR_CALL.toString());
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception("Adding Transactional email to the queue failed");
		}

	}

	@Override
	public void consume(MessageSenderModel messageSenderModel) throws IOException {
		// TODO Auto-generated method stub
		RabbitMqClient.consumeMsgs(new IvrCallsConsumer(), TemplateType.IVR_CALL.toString());

	}
	public void consumeThis(String queueName) throws IOException {
		// TODO Auto-generated method stub
		// RabbitMqClient.consumeIvrCalls(new IvrCallsConsumer(),
		// TemplateType.IVR_CALL.toString());
		// AMQP.Queue.DeclareOk dok =
		// RabbitMqClient.getIVRChannel().queueDeclare("IVR_CALLS", true, false,
		// false, null);
		// dok.getMessageCount();
		// System.out.println(dok.getMessageCount() + "----------------");
		RabbitMqClient.getIVRChannel();
		
		RabbitMqClient.bindIVRExgChannel("IVR_CALLS");

		Consumer consumer = new DefaultConsumer(RabbitMqClient.getIVRChannel()) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {
				String data = new String(body, "UTF-8");
				System.out.println(" [x] Received '" + data + "'");

				try {
					ivrCallService.CallAndPutInDB(data);
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				long deliveryTag = envelope.getDeliveryTag();
				// (process the message components here ...)
				RabbitMqClient.getIVRChannel().basicAck(deliveryTag, false);

			}
		};
		try {
			RabbitMqClient.getIVRChannel().basicConsume(queueName, autoAck, consumer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Some issue in consuming msg");
			e.printStackTrace();
		}

	}

	@Override
	public String getRequestId(IMessageBean messageBean) {
		// TODO Auto-generated method stub
		return null;
	}

}
