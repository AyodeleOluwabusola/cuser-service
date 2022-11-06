package com.coronation.captr.userservice.config;

import com.coronation.captr.userservice.util.AppProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author toyewole
 */

@Configuration
public class Config {

    @Autowired
    AppProperties appProperties;


    @Bean
    public Queue tokenGenerationQueue () {
        return new Queue(appProperties.getNotificationQueue(), true);
    }




}
