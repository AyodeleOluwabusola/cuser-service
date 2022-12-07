package com.coronation.captr.userservice.respositories;

import com.coronation.captr.userservice.entities.RaiseUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Nneoma
 */
public interface RaiseUserRepository extends JpaRepository<RaiseUser, Long> {

    boolean existsByEmail(String email);

}
