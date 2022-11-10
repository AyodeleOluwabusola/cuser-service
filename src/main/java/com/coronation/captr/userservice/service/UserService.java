package com.coronation.captr.userservice.service;

import com.coronation.captr.userservice.entities.User;
import com.coronation.captr.userservice.enums.IResponseEnum;
import com.coronation.captr.userservice.interfaces.IResponseData;
import com.coronation.captr.userservice.pojo.MessagePojo;
import com.coronation.captr.userservice.pojo.ResponseData;
import com.coronation.captr.userservice.pojo.UserPojo;
import com.coronation.captr.userservice.pojo.req.UserRequest;
import com.coronation.captr.userservice.respositories.IUserRespository;
import com.coronation.captr.userservice.util.AppProperties;
import com.coronation.captr.userservice.util.Constants;
import com.coronation.captr.userservice.util.ProxyTransformer;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author toyewole
 */

@Transactional
@Service
public class UserService {

    @Autowired
    IUserRespository iUserRespository;


    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    AppProperties appProperties;

    public IResponseData<UserPojo> createUser(UserRequest request) {

        IResponseData<UserPojo> response = isRequestValid(request);

        if (IResponseEnum.SUCCESS.getCode() != response.getCode()) {
            return response;
        }

        if (iUserRespository.existsUserByEmail(request.getEmail())) {
            response.setResponse(IResponseEnum.EMAIL_EXIST);
            return response;
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPassword(hashPassword(request.getPassword()));
        String token = UUID.randomUUID().toString();
        user.setConfirmationCode(hashPassword(token));

        iUserRespository.save(user);

        sendEmailConfirmation(user, token);

        response.setData(ProxyTransformer.transformUserToUserPojo(user));
        response.setResponse(IResponseEnum.SUCCESS);
        return response;
    }

    private void sendEmailConfirmation(User user, String token) {
        MessagePojo message = new MessagePojo();


        var expTime = LocalDateTime.now().plusMinutes(appProperties.getEmailConfirmationExpTime());
        String link = String.format(appProperties.getEmailConfirmationLink(), user.getEmail(), token);
        message.setMessageBody(String.format(appProperties.getEmailConfirmationMessage(), user.getFirstName(), link, expTime.format(Constants.DATE_TIME_FORMATTER)));
        message.setRecipient(user.getEmail());
        message.setSource("user-service");
        message.setSubject("Email Confirmation");
        message.setRequestTime(LocalDateTime.now().format(Constants.DATE_TIME_FORMATTER));

        rabbitTemplate.convertAndSend(appProperties.getNotificationExchange(), appProperties.getRoutingKey(),message);

    }

    private String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }

    private IResponseData<UserPojo> isRequestValid(UserRequest request) {
        ResponseData<UserPojo> response = new ResponseData<>();
        if (StringUtils.isBlank(request.getEmail())) {
            response.setDescription("Kindly provide an Email Address ");
            return response;
        }

        if (StringUtils.isBlank(request.getFirstName())) {
            response.setDescription("Kindly provide a First name ");
            return response;
        }

        if (StringUtils.isBlank(request.getLastName())) {
            response.setDescription("Kindly provide a Last name");
            return response;
        }

        if (StringUtils.isBlank(request.getPassword())) {
            response.setDescription("Kindly provide a password");
            return response;
        }

        if (StringUtils.equalsIgnoreCase(request.getPassword(), request.getConfirmPassword())) {
            response.setDescription("Password and confirm password must the same ");
            return response;
        }

        return response;
    }

}
