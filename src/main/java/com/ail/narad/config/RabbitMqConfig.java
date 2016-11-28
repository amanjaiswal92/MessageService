package com.ail.narad.config;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@EqualsAndHashCode(callSuper = false)
@Component
@ConfigurationProperties(prefix = "Custom.rabbitmq")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RabbitMqConfig {
    String host;
    String username;
    String password;
    long timeout;
    long threadLifeTime;
    long sleepInterval;
    int poolSize;
}
