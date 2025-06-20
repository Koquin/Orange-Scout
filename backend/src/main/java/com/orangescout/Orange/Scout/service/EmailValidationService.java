package com.orangescout.Orange.Scout.service;

import com.orangescout.Orange.Scout.exception.ResourceNotFoundException;
import com.orangescout.Orange.Scout.repository.AppUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class EmailValidationService {

    private static final Logger logger = LoggerFactory.getLogger(EmailValidationService.class);

    private final AppUserRepository appUserRepository;

    private final Map<String, ValidationData> validationCache = new ConcurrentHashMap<>();

    @Value("${app.email.validation.code-expiration-minutes:15}")
    private int codeExpirationMinutes;

    public EmailValidationService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    public String generateValidationCode(String email) {
        appUserRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.warn("Attempt to generate validation code for non-existent email: {}", email);
                    return new ResourceNotFoundException("User not found with email: " + email);
                });

        String code = String.valueOf(100000 + new Random().nextInt(900000));
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(codeExpirationMinutes);
        validationCache.put(email, new ValidationData(code, expirationTime));
        logger.info("Validation code generated for '{}'. Code: {}. Expires at: {}", email, code, expirationTime);
        return code;
    }

    public boolean validateCode(String email, String code) {
        ValidationData data = validationCache.get(email);

        if (data == null) {
            logger.warn("Attempt to validate code for email with no generated code or already validated: {}", email);
            return false;
        }

        if (!data.getCode().equals(code)) {
            logger.warn("Code validation failed for '{}': Incorrect code.", email);
            return false;
        }

        if (LocalDateTime.now().isAfter(data.getExpiration())) {
            logger.warn("Code validation failed for '{}': Expired code.", email);
            validationCache.remove(email);
            return false;
        }

        validationCache.remove(email);
        logger.info("Code for '{}' validated successfully.", email);
        return true;
    }

    private static class ValidationData {
        private final String code;
        private final LocalDateTime expiration;

        public ValidationData(String code, LocalDateTime expiration) {
            this.code = code;
            this.expiration = expiration;
        }

        public String getCode() {
            return code;
        }

        public LocalDateTime getExpiration() {
            return expiration;
        }
    }
}