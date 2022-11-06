package com.coronation.captr.userservice.service;

import com.coronation.captr.userservice.entities.User;
import com.coronation.captr.userservice.enums.IResponseEnum;
import com.coronation.captr.userservice.interfaces.IResponse;
import com.coronation.captr.userservice.interfaces.IResponseData;
import com.coronation.captr.userservice.pojo.MessagePojo;
import com.coronation.captr.userservice.pojo.Response;
import com.coronation.captr.userservice.pojo.ResponseData;
import com.coronation.captr.userservice.pojo.req.UserRequest;
import com.coronation.captr.userservice.respositories.IUserRespository;
import com.coronation.captr.userservice.util.AppProperties;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author toyewole
 */

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

    public IResponseData<User> createUser(UserRequest request) {

        IResponseData<User> response = isRequestValid(request);

        if (IResponseEnum.SUCCESS.getCode() != response.getCode()) {
            return response;
        }

        if (iUserRespository.existsUserByEmail(request.getEmail())) {
            response.setResponse(IResponseEnum.EMAIL_EXIST);
            return response;
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setFirstname(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPassword(hashPassword(request.getPassword()));
        user.setConfirmationCode(UUID.randomUUID().toString());

        iUserRespository.save(user);

        sendEmailConfirmation(user);

        response.setResponse(IResponseEnum.SUCCESS);
        return response;
    }

    private void sendEmailConfirmation(User user) {
        MessagePojo message = new MessagePojo();

        String token = user.getConfirmationCode();

        //TODO make this configurable
        message.setMessage(String.format(appProperties.getEmailConfirmationMessage(), user.getEmail(), token, "24", "hrs"));
        message.setRecipient(user.getEmail());
        message.setSource("user-service");
        message.setRequestTime(LocalDateTime.now()); //TODO convert to test

        rabbitTemplate.convertAndSend(message);

    }

    private String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }

    private IResponseData<User> isRequestValid(UserRequest request) {
        ResponseData<User> response = new ResponseData<>();
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
