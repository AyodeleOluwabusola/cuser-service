package com.coronation.captr.userservice.resource;

import com.coronation.captr.userservice.interfaces.IResponse;
import com.coronation.captr.userservice.pojo.req.UserRequest;
import com.coronation.captr.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author toyewole
 */

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    @RequestMapping("/sign-up")
    public IResponse createUser(@RequestBody UserRequest request) {
        return userService.createUser(request);
    }


}
