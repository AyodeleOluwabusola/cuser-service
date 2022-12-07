package com.coronation.captr.userservice.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Nneoma
 */

@Getter
@Setter
@Entity
@Table(name = "RAISE_USER")
public class RaiseUser extends CRUser {

    @Column(name = "TERMS_AND_CONDITIONS_ACCEPTED")
    private Boolean termsAndConditionsAccepted;

    @Column(name = "MARKETING_CONSENT")
    private Boolean isMarketingConsent;

    @Column(name = "POLICY_CONSENT")
    private Boolean isPolicyConsent;

}
