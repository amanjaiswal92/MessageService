package com.ail.narad.messagingimpl;

import com.ail.narad.config.RabbitMqConfig;
import com.ail.narad.domain.Audit_logs;
import com.ail.narad.domain.enumeration.AuditEvent;
import com.ail.narad.models.MessageSenderModel;
import com.ail.narad.service.AddToLogService;
import com.ailiens.common.RabbitMqConnectionManager;
import com.ailiens.common.RabbitMqUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.QueueingConsumer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by tech on 13/10/16.
 */
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Data
@Component
public  class MessageReceiver {
    private static ObjectMapper mapper = new ObjectMapper();

    @Autowired
    RabbitMqConfig rabbitMqConfig;

    @Autowired
    AddToLogService addToLogService;

    @Autowired
    MessageSenderModel messageSenderModel;

    @Autowired
    SendToServiceProvider sendToServiceProvider;

    String threadName;

    @Wither
    String queueName;

    boolean autoAck=true;

    long startTime= System.currentTimeMillis();


    public void consumeMessages()  throws Exception {
        Connection connection=null;
        Channel channel=null;
        try {
            connection = RabbitMqConnectionManager.getConnection();
            channel = connection.createChannel();

            channel.queueDeclare(queueName, true, false, false, null);
            log.info("listening to queue {}", queueName);

            QueueingConsumer consumer = new QueueingConsumer(channel);

            channel.basicConsume(queueName, autoAck, consumer);

            long timeout =rabbitMqConfig.getTimeout();
            long threadLifeTime =rabbitMqConfig.getThreadLifeTime();

            while (true) {
                QueueingConsumer.Delivery delivery = consumer.nextDelivery();

                if(delivery!=null) {

                    String messageStr = new String(delivery.getBody());
                    log.info("Message received from queue : " + messageStr);
                    JSONObject message = new JSONObject(messageStr);
                    String id =(String)message.get("requestId");
                    addToLogService.addToAuditTable(new Audit_logs(id,AuditEvent.DEQUEUED_FROM_QUEUE, ""));
                    String type = (String)message.get("type");
                    sendToServiceProvider.provider(id,type,messageStr);
                    //TemplateType.valueOf(StringUtils.upperCase(type)
//                    IMessageBean messageBean =
//                    TransactionalEmailMessage messageBean = (TransactionalEmailMessage) TransactionalEmailMessage.deSerialize(message);

//                    messageSenderModel.sendMessage(type,message);
//                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

                }


            }

        }
        catch(Exception e)
        {
            log.info("got error while reading queue {} {}",queueName, ExceptionUtils.getStackTrace(e));
            throw e;

        }
        finally {
            RabbitMqUtil.ensureClosure(connection,channel);
        }
    }
}
