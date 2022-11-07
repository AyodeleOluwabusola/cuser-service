package com.coronation.captr.userservice.util;

import com.coronation.captr.userservice.entities.User;
import com.coronation.captr.userservice.pojo.UserPojo;
import lombok.experimental.UtilityClass;

/**
 * @author toyewole
 */
@UtilityClass
public class ProxyTransformer {

    public UserPojo transformUserToUserPojo(User user){

        UserPojo userPojo = new UserPojo();
        userPojo.setEmail(user.getEmail());
        userPojo.setFirstName(user.getFirstName());
        userPojo.setLastName(user.getLastName());

        return userPojo;
    }
}
