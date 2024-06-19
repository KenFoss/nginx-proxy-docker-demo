package com.auth.demo.jpa.repository;

import com.auth.demo.jpa.model.GoogleUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GoogleUserRepository extends JpaRepository<GoogleUser, Long> {
    // Additional custom query methods if needed
    GoogleUser findByEmail(String email);
    Optional<GoogleUser> findByGoogleId(String googleId);
}
