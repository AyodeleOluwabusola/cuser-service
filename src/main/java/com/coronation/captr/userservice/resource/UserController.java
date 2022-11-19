package com.coronation.captr.userservice.resource;

import com.coronation.captr.userservice.interfaces.IResponse;
import com.coronation.captr.userservice.pojo.request.CompanyProfileRequest;
import com.coronation.captr.userservice.pojo.request.UserRequest;
import com.coronation.captr.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author toyewole
 */

@RestController
@RequestMapping("/")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    @RequestMapping("/sign-up")
    public IResponse createUser(@RequestBody UserRequest request) {
        return userService.createUser(request);
    }

    @PostMapping
    @RequestMapping("create-company-profile")
    public IResponse createCompanyProfile(@RequestBody @Valid CompanyProfileRequest request) {
        return userService.createCompanyProfile(request);
    }

    @GetMapping("pending-company-profile/{loggedInUser}")
    public IResponse createCompanyProfile(@PathVariable("loggedInUser") long id) {
        return userService.retrieveCompanyProfile(id);
    }

}
