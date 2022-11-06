package com.coronation.captr.userservice.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

/**
 * @author toyewole
 */
@Getter
@Setter
@RequestScope
@Component
public class RequestBean {

    private String principal;
}
