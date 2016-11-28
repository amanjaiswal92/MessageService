package com.ail.narad.config;

import com.ail.narad.messagingimpl.MessageConsumer;
import com.ailiens.common.PoolType;
import com.ailiens.common.RabbitMqConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.inject.Provider;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

/**
 * Created by tech on 13/10/16.
 */
@Component
public class Initialize {

    @Autowired
    Provider<MessageConsumer> messageConsumerProvider;

    @Autowired
    RabbitMqConfig rabbitMqConfig;

    ExecutorService executorService = Executors.newFixedThreadPool(10);

    @PostConstruct
    public void setup() throws IOException, TimeoutException, InterruptedException {
        RabbitMqConnectionManager.setPoolSize(2);
        RabbitMqConnectionManager.setPoolType(PoolType.EAGER);
//        RabbitMqConnectionManager.createConnectionPool("default", ApplicationProperties.getCustomProperty("rabbitmq.host").toString(),ApplicationProperties.getCustomProperty("rabbitmq.username").toString(),ApplicationProperties.getCustomProperty("rabbitmq.password").toString());
        RabbitMqConnectionManager.createConnectionPool("default", rabbitMqConfig.getHost(),rabbitMqConfig.getUsername(),rabbitMqConfig.getPassword());
        //to get data from queue by two thread
        executorService.submit(messageConsumerProvider.get());
        executorService.submit(messageConsumerProvider.get());
    }
}
