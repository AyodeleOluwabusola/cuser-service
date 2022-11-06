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

    private String notificationQueue = "myQueue";
    private String notificationExchange;
    private String emailConfirmationMessage = "Dear %s, \n Here is your email confirmation link. %s . Kindly note this link would expire in the next %s %s";

}
