package com.coronation.captr.userservice.pojo;


import lombok.Getter;
import lombok.Setter;


import java.time.LocalDateTime;

/**
 * @author toyewole
 */
@Getter
@Setter
public class MessagePojo {

    private String message;
    private String subject;
    private String messageType;
    private String recipient;
    private String source;
    private String requestTime;
}
