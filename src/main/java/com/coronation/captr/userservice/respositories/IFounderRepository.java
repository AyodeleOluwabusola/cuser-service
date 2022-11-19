package com.coronation.captr.userservice.respositories;

import com.coronation.captr.userservice.entities.Founder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author AuodeleOluwabusola
 */
public interface IFounderRepository extends JpaRepository<Founder, Long> {

    @Override
    Optional<Founder> findById(Long id);
}
