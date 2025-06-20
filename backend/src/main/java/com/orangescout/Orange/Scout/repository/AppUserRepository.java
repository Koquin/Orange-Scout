package com.orangescout.Orange.Scout.repository;

import com.orangescout.Orange.Scout.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying; // Importar @Modifying
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional; // Importar @Transactional

import java.time.LocalDateTime; // Importar LocalDateTime
import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByEmail(String email);

    boolean existsByEmail(String email);

    @Modifying
    @Transactional
    int deleteByValidatedFalseAndCreatedAtBefore(LocalDateTime thresholdTime);

}