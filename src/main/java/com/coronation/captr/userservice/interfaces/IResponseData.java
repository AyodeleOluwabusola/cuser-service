package com.coronation.captr.userservice.interfaces;

import com.coronation.captr.userservice.enums.IResponseEnum;

/**
 * @author toyewole
 */
public interface IResponseData <T>  extends  IResponse{

    T getData();

    void setResponse(IResponseEnum iResponseEnum);
}
