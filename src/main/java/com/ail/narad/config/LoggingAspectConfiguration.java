package com.ail.narad.config;

import com.ail.narad.aop.logging.LoggingAspect;
import org.springframework.context.annotation.*;

@Configuration
@EnableAspectJAutoProxy
public class LoggingAspectConfiguration {

    @Bean
    @Profile({Constants.SPRING_PROFILE_DEVELOPMENT, Constants.SPRING_PROFILE_QA})
    public LoggingAspect loggingAspect() {
        return new LoggingAspect();
    }
}
