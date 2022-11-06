package com.coronation.captr.userservice.pojo.req;

import lombok.Getter;
import lombok.Setter;

/**
 * @author toyewole
 */
@Getter
@Setter
public class UserRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String confirmPassword;

}
