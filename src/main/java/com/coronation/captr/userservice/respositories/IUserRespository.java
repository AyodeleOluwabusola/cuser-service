package com.coronation.captr.userservice.respositories;

import com.coronation.captr.userservice.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author toyewole
 */
public interface IUserRespository extends JpaRepository<User, Long> {

    boolean existsUserByEmail(String email);
}
