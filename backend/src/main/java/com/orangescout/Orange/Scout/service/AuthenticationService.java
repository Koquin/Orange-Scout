package com.orangescout.Orange.Scout.service;

import com.orangescout.Orange.Scout.exception.AuthenticationException;
import com.orangescout.Orange.Scout.model.AppUser;
import com.orangescout.Orange.Scout.repository.AppUserRepository;
import com.orangescout.Orange.Scout.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthenticationService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public String authenticate(String email, String password) {
        logger.info("Authentication attempt received for email: {}", email);

        AppUser appUser = appUserRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.warn("Authentication failed: User not found with email: {}", email);
                    return new AuthenticationException("Invalid credentials.");
                });

        if (!passwordEncoder.matches(password, appUser.getPassword())) {
            logger.warn("Authentication failed: Invalid password for email: {}", email);
            throw new AuthenticationException("Invalid credentials.");
        }

        String token = jwtUtil.generateToken(appUser);
        logger.info("Authentication successful for email: {}", email);
        return token;
    }
}