package com.coronation.captr.userservice.pojo.request;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Nneoma
 */
@Getter
@Setter
public class RaiseUserRequest extends UserRequest {
    private boolean termsAndConditionsAccepted;
    private boolean isMarketingConsent;
    private boolean isPolicyConsent;

}
