package com.orangescout.Orange.Scout.controller;

import com.orangescout.Orange.Scout.dto.LoginRequest;
import com.orangescout.Orange.Scout.dto.LoginResponse;
import com.orangescout.Orange.Scout.dto.RegisterRequest;
import com.orangescout.Orange.Scout.dto.ValidationRequest;
import com.orangescout.Orange.Scout.exception.AuthenticationException;
import com.orangescout.Orange.Scout.exception.BadRequestException;
import com.orangescout.Orange.Scout.exception.ResourceNotFoundException;
import com.orangescout.Orange.Scout.exception.ValidationCodeException;
import com.orangescout.Orange.Scout.model.AppUser;
import com.orangescout.Orange.Scout.security.JwtUtil;
import com.orangescout.Orange.Scout.service.AppUserService;
import com.orangescout.Orange.Scout.service.AuthenticationService;
import com.orangescout.Orange.Scout.service.EmailService;
import com.orangescout.Orange.Scout.service.EmailValidationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationService authenticationService;
    private final EmailValidationService emailValidationService;
    private final EmailService emailService;
    private final AppUserService appUserService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationService authenticationService, EmailValidationService emailValidationService,
                          EmailService emailService, AppUserService appUserService, JwtUtil jwtUtil) {
        this.authenticationService = authenticationService;
        this.emailValidationService = emailValidationService;
        this.emailService = emailService;
        this.appUserService = appUserService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        logger.info("Login request received for email: {}", loginRequest.getEmail());
        String token = authenticationService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
        return ResponseEntity.ok(new LoginResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        logger.info("Registration request received for email: {}", registerRequest.getEmail());

        AppUser newAppUser = new AppUser(
                registerRequest.getEmail(),
                registerRequest.getPassword(),
                registerRequest.getUsername(),
                "ROLE_USER"
        );
        AppUser registeredAppUser = appUserService.registerAppUser(newAppUser);

        String validationCode = emailValidationService.generateValidationCode(registeredAppUser.getEmail());
        emailService.sendEmail(registeredAppUser.getEmail(), "Orange SCOUT Validation Code", "Your validation code is: " + validationCode);
        logger.info("Validation code sent to: {}", registeredAppUser.getEmail());

        String token = authenticationService.authenticate(registeredAppUser.getEmail(), registerRequest.getPassword());
        return new ResponseEntity<>(new LoginResponse(token), HttpStatus.CREATED);
    }

    @PostMapping("/isTokenExpired")
    public ResponseEntity<Boolean> isTokenExpired(@RequestBody Map<String, String> body) {
        String token = body.get("token");
        if (token == null || token.isEmpty()) {
            throw new BadRequestException("Token cannot be null or empty.");
        }
        logger.debug("Checking token expiration: {}", token);
        try {
            boolean expired = jwtUtil.isTokenExpired(token);
            return ResponseEntity.ok(expired);
        } catch (AuthenticationException e) {
            logger.warn("Invalid or malformed token during expiration check: {}", e.getMessage());
            return ResponseEntity.ok(true);
        }
    }

    @GetMapping("/isValidated")
    public ResponseEntity<Boolean> isValidated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        logger.info("Checking validation status for email: {}", userEmail);
        boolean isValidated = appUserService.isValidated(userEmail);
        return ResponseEntity.ok(isValidated);
    }

    @PostMapping("/resendValidationCode")
    public ResponseEntity<String> resendValidationCode() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        logger.info("Resend validation code request for email: {}", userEmail);
        try {
            String validationCode = emailValidationService.generateValidationCode(userEmail);
            emailService.sendEmail(userEmail, "Orange SCOUT Validation Code Resend", "Your new validation code is: " + validationCode);
            return ResponseEntity.ok("Validation code resent to " + userEmail + ".");
        } catch (ResourceNotFoundException e) {
            logger.warn("Attempted to resend code for non-existent (authenticated) email: {}", userEmail, e);
            throw new ResourceNotFoundException("User not found.");
        } catch (Exception e) {
            logger.error("Error resending validation code to {}: {}", userEmail, e.getMessage(), e);
            throw new BadRequestException("Failed to resend validation code.");
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<String> validate(@Valid @RequestBody ValidationRequest validationRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        String code = validationRequest.getCode();
        logger.info("Attempting to validate email for '{}' with code.", userEmail);

        try {
            if (emailValidationService.validateCode(userEmail, code)) {
                appUserService.updateValidationStatusById(appUserService.getAppUserByEmail(userEmail).getId(), true);
                logger.info("Email '{}' validated successfully.", userEmail);
                return ResponseEntity.ok("Email successfully validated!");
            } else {
                logger.warn("Failed to validate code for '{}'.", userEmail);
                throw new ValidationCodeException("Invalid or expired validation code.");
            }
        } catch (ResourceNotFoundException e) {
            logger.error("User not found during validation: {}", userEmail, e);
            throw new ResourceNotFoundException("User not found during validation.");
        } catch (Exception e) {
            logger.error("Unexpected error during email validation for '{}': {}", userEmail, e.getMessage(), e);
            throw new BadRequestException("Error validating email.");
        }
    }
}