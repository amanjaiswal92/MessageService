package com.ail.narad.messagingimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.inject.Provider;

/**
 * Created by tech on 13/10/16.
 */
@Component
public class MessageConsumer implements Runnable {

    @Autowired
    Provider<MessageReceiver> messageConsumerProvider;

    @Override
    public void run() {
        while(true) {
            try {
                messageConsumerProvider.get().withQueueName("naradNew").consumeMessages();
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
