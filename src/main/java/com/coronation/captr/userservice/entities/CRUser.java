package com.coronation.captr.userservice.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * @author Nneoma
 */

@Getter
@Setter
@MappedSuperclass
public class CRUser extends BaseEntity {

    @Column(name = "FIRST_NAME", nullable = false)
    private String firstName;

    @Column(name = "LAST_NAME", nullable = false)
    private String lastName;

    @Column(name = "PASSSWORD", nullable = false)
    private String password;

    @Column(name = "EMAIL", unique = true, nullable = false)
    private String email;

    @Column(name = "CONFIRMATION_CODE")
    private String confirmationCode;

    @Column(name = "PENDING_REQUEST")
    private Long pendingRequestPk;

    @Column(name = "CONFIRMATION_TIMESTAMP")
    private LocalDateTime emailConfirmationTimestamp;


}
