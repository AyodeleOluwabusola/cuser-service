package com.coronation.captr.userservice.respositories;

import com.coronation.captr.userservice.entities.CompanyProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author AuodeleOluwabusola
 */
public interface ICompanyProfileRepository extends JpaRepository<CompanyProfile, Long> {

    @Override
    Optional<CompanyProfile> findById(Long aLong);
}
