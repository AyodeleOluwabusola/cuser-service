package com.coronation.captr.userservice.service;

import com.coronation.captr.userservice.entities.CTUser;
import com.coronation.captr.userservice.enums.IResponseEnum;
import com.coronation.captr.userservice.interfaces.IResponseData;
import com.coronation.captr.userservice.pojo.MessagePojo;
import com.coronation.captr.userservice.pojo.ResponseData;
import com.coronation.captr.userservice.pojo.UserPojo;
import com.coronation.captr.userservice.pojo.request.UserRequest;
import com.coronation.captr.userservice.respositories.IUserRepository;
import com.coronation.captr.userservice.util.AppProperties;
import com.coronation.captr.userservice.util.CommonLogic;
import com.coronation.captr.userservice.util.Constants;
import com.coronation.captr.userservice.util.ProxyTransformer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author toyewole
 */

@Transactional
@Service
@Slf4j
public class UserService {

    @Autowired
    IUserRepository iUserRepository;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    CommonLogic commonLogic;

    @Autowired
    AppProperties appProperties;

    public IResponseData<UserPojo> createUser(UserRequest request) {

        IResponseData<UserPojo> response = isRequestValid(request);

        if (IResponseEnum.SUCCESS.getCode() != response.getCode()) {
            return response;
        }

        if (iUserRepository.existsUserByEmail(request.getEmail())) {
            response.setResponse(IResponseEnum.EMAIL_EXIST);
            return response;
        }

        CTUser user = new CTUser();
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPassword(commonLogic.hashPassword(request.getPassword()));
        String token = UUID.randomUUID().toString();
        user.setConfirmationCode(commonLogic.hashPassword(token));

        iUserRepository.save(user);

        sendEmailConfirmation(user, token);

        response.setData(ProxyTransformer.transformUserToUserPojo(user));
        response.setResponse(IResponseEnum.SUCCESS);
        return response;
    }

    private void sendEmailConfirmation(CTUser user, String token) {
        MessagePojo message = new MessagePojo();


        var expTime = LocalDateTime.now().plusMinutes(appProperties.getEmailConfirmationExpTime());
        String link = String.format(appProperties.getEmailConfirmationLink(), user.getEmail(), token);
        message.setMessageBody(String.format(appProperties.getEmailConfirmationMessage(), user.getFirstName(),
                link, expTime.format(Constants.DATE_TIME_FORMATTER)));
        message.setRecipient(user.getEmail());
        message.setSource("user-service");
        message.setSubject("Email Confirmation");
        message.setRequestTime(LocalDateTime.now().format(Constants.DATE_TIME_FORMATTER));

        rabbitTemplate.convertAndSend(appProperties.getNotificationExchange(), appProperties.getRoutingKey(), message);

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
