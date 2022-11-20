package com.coronation.captr.userservice.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author toyewole
 */
@Getter
@Setter
@ConfigurationProperties
@Component
public class AppProperties {

    private String notificationQueue = "email_sender_queue";
    private String notificationExchange = "exchange.email_sender_exchange";
    private String routingKey = "routing.email_sender";
    private String emailConfirmationMessage = "Dear %s, \n Here is your email confirmation link. %s . Kindly note this link would expire by %s ";
    private int emailConfirmationExpTime = 360;
    private String emailConfirmationLink= "<a href=\"http://localhost:8000/captr/access/verify-email?email=%s&code=%s\">Confirm password!!</a>";


    private String activityQueue = "activity_log_queue";
    private String activityExchange = "exchange.activity_log_exchange";
    private String activityLogRoutingKey = "routing.activty_log";
}
