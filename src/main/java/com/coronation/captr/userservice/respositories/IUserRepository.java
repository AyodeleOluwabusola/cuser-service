package com.coronation.captr.userservice.respositories;

import com.coronation.captr.userservice.entities.CTUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author toyewole
 */
public interface IUserRepository extends JpaRepository<CTUser, Long> {

    boolean existsUserByEmail(String email);

    boolean existsUserById(Long id);

    @Override
    Optional<CTUser> findById(Long userId);

}
