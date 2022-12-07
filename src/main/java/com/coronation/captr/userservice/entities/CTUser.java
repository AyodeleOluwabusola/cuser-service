package com.coronation.captr.userservice.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author toyewole
 */

@Getter
@Setter
@Entity
@Table(name = "CT_USER")
public class CTUser extends CRUser {

}
