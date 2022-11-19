package com.coronation.captr.userservice.enums;

import com.coronation.captr.userservice.interfaces.IResponse;

/**
 * @author toyewole
 */
public enum IResponseEnum implements IResponse {

    NO_PENDING_REQUEST(-5, "No pending Request found"),
    NO_USER_FOUND(-4, "No user found"),
    INVALID_REQUEST(-3, "Invalid Request"),
    EMAIL_EXIST(-2, "Email already exist"),
    ERROR(-1, "Error occurred while processing request"),
    SUCCESS(0, "Request processed successfully"),
    ;

    int code;
    String desc;

    IResponseEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getDescription() {
        return desc;
    }
}
