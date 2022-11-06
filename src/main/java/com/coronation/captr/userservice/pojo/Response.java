package com.coronation.captr.userservice.pojo;

import com.coronation.captr.userservice.interfaces.IResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * @author toyewole
 */
@Getter
@Setter
public class Response implements IResponse {
    int code;
    String description;
}
