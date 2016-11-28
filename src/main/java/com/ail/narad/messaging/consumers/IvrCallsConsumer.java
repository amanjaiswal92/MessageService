package com.ail.narad.messaging.consumers;

import java.io.IOException;

import com.ail.narad.messagingimpl.RabbitMqClient;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;

public class IvrCallsConsumer implements Consumer {

	@Override
	public void handleConsumeOk(String consumerTag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleCancelOk(String consumerTag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleCancel(String consumerTag) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
		// TODO Auto-generated method stub
		
		String message = new String(body, "UTF-8");

	    System.out.println(" [x] Received '" + message + "'");
	    try {
	    	 System.out.println("------------------------------");
	    	 System.out.println(" [x] Received '" + message + "'");
	    } finally {
	      System.out.println(" [x] Done");
	     
	    }
	  }
		
	

	@Override
	public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleRecoverOk(String consumerTag) {
		// TODO Auto-generated method stub
		
	}

}
