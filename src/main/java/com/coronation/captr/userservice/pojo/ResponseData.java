package com.coronation.captr.userservice.pojo;

import com.coronation.captr.userservice.enums.IResponseEnum;
import com.coronation.captr.userservice.interfaces.IResponseData;
import lombok.Getter;
import lombok.Setter;

/**
 * @author toyewole
 */

@Getter
@Setter
public class ResponseData<T> implements IResponseData<T> {

    private int code;
    private String description;
    private T data;


    @Override
    public void setResponse(IResponseEnum iResponseEnum) {
        this.code = iResponseEnum.getCode();
        this.description = iResponseEnum.getDescription();
    }


}
