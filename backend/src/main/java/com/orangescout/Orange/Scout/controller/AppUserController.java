package com.orangescout.Orange.Scout.controller;

import com.orangescout.Orange.Scout.dto.AppUserDTO;
import com.orangescout.Orange.Scout.exception.BadRequestException;
import com.orangescout.Orange.Scout.service.AppUserService;
import com.orangescout.Orange.Scout.service.EmailService;
import com.orangescout.Orange.Scout.service.EmailValidationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app-users")
public class AppUserController {

    private static final Logger logger = LoggerFactory.getLogger(AppUserController.class);

    private final AppUserService appUserService;
    private final EmailValidationService emailValidationService;
    private final EmailService emailService;

    public AppUserController(AppUserService appUserService, EmailValidationService emailValidationService, EmailService emailService) {
        this.appUserService = appUserService;
        this.emailValidationService = emailValidationService;
        this.emailService = emailService;
    }

    @GetMapping
    public ResponseEntity<List<AppUserDTO>> getAllAppUsers() {
        logger.info("Request to fetch all users (ADMIN ONLY).");
        List<AppUserDTO> appUsers = appUserService.getAllAppUsers().stream()
                .map(AppUserDTO::fromEntity)
                .toList();
        logger.debug("Returning {} users.", appUsers.size());
        return ResponseEntity.ok(appUsers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppUserDTO> getAppUserById(@PathVariable Long id) {
        logger.info("Request to fetch user with ID: {}", id);
        AppUserDTO appUserDTO = AppUserDTO.fromEntity(appUserService.getAppUserById(id));
        logger.debug("User with ID {} found: {}", id, appUserDTO.getId());
        return ResponseEntity.ok(appUserDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppUserDTO> updateAppUser(@Valid @RequestBody AppUserDTO appUserDTO, @PathVariable Long id) {
        logger.info("Request to update user with ID: {}", id);

        if (appUserDTO.getId() == null || !appUserDTO.getId().equals(id)) {
            logger.warn("ID inconsistency: Path ID {} vs Body ID {}", id, appUserDTO.getId());
            throw new BadRequestException("The ID in the request body must match the ID in the URL.");
        }

        AppUserDTO updatedAppUser = appUserService.updateAppUser(id, appUserDTO);
        logger.info("User with ID {} updated successfully.", updatedAppUser.getId());
        return ResponseEntity.ok(updatedAppUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppUser(@PathVariable Long id) {
        logger.info("Request to delete user with ID: {}", id);
        appUserService.deleteAppUser(id);
        logger.info("User with ID {} deleted successfully.", id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    @GetMapping("/premium")
    public ResponseEntity<Boolean> isPremium() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        logger.info("Checking premium status for email: {}", userEmail);
        boolean isPremium = appUserService.isPremium(userEmail);
        return ResponseEntity.ok(isPremium);
    }

    @PatchMapping("/{id}/validation-status")
    public ResponseEntity<AppUserDTO> updateValidationStatus(@PathVariable Long id, @RequestParam boolean validated) {
        logger.info("Request to update validation status for user ID {} to {}.", id, validated);
        AppUserDTO updatedUser = appUserService.updateValidationStatusById(id, validated);
        logger.info("Validation status for user ID {} updated to {}.", id, validated);
        return ResponseEntity.ok(updatedUser);
    }

    @PatchMapping("/{id}/premium-status")
    public ResponseEntity<AppUserDTO> updatePremiumStatus(@PathVariable Long id, @RequestParam boolean premium) {
        logger.info("Request to update premium status for user ID {} to {}.", id, premium);
        AppUserDTO updatedUser = appUserService.updatePremiumStatusById(id, premium);
        logger.info("Premium status for user ID {} updated to {}.", id, premium);
        return ResponseEntity.ok(updatedUser);
    }
}