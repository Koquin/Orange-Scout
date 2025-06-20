package com.orangescout.Orange.Scout.service;

import com.orangescout.Orange.Scout.dto.AppUserDTO;
import com.orangescout.Orange.Scout.exception.ResourceNotFoundException;
import com.orangescout.Orange.Scout.exception.ResourceAlreadyExistsException;
import com.orangescout.Orange.Scout.model.AppUser;
import com.orangescout.Orange.Scout.repository.AppUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class AppUserService {

    private static final Logger logger = LoggerFactory.getLogger(AppUserService.class);

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AppUserService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public AppUser registerAppUser(AppUser appUser) {
        if (appUserRepository.findByEmail(appUser.getEmail()).isPresent()) {
            logger.warn("Attempt to register with an existing email: {}", appUser.getEmail());
            throw new ResourceAlreadyExistsException("User with email '" + appUser.getEmail() + "' already exists.");
        }
        String encodedPassword = passwordEncoder.encode(appUser.getPassword());
        appUser.setPassword(encodedPassword);
        appUser.setRole("ROLE_USER");
        appUser.setValidated(false);

        AppUser savedAppUser = appUserRepository.save(appUser);
        logger.info("New user registered with email: {}", savedAppUser.getEmail());
        return savedAppUser;
    }

    @Transactional
    public void deleteAppUser(Long id) {
        if (!appUserRepository.existsById(id)) {
            logger.warn("Attempt to delete non-existent user with ID: {}", id);
            throw new ResourceNotFoundException("User not found with ID: " + id);
        }
        appUserRepository.deleteById(id);
        logger.info("User with ID {} deleted successfully.", id);
    }

    public List<AppUser> getAllAppUsers() {
        logger.info("Fetching all users.");
        return appUserRepository.findAll();
    }

    public AppUser getAppUserById(Long id) {
        return appUserRepository.findById(id).orElseThrow(
                () -> {
                    logger.warn("Attempt to fetch non-existent user with ID: {}", id);
                    return new ResourceNotFoundException("User not found with ID: " + id);
                }
        );
    }

    public AppUser getAppUserByEmail(String email) {
        return appUserRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.warn("User not found with email: {}", email);
                    return new ResourceNotFoundException("User not found with email: " + email);
                });
    }

    @Transactional
    public AppUserDTO updateAppUser(Long id, AppUserDTO appUserDTO) {
        logger.info("Updating user with ID: {}", id);
        AppUser existingUser = appUserRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Attempt to update non-existent user with ID: {}", id);
                    return new ResourceNotFoundException("User not found with ID: " + id);
                });

        existingUser.setEmail(appUserDTO.getEmail());
        existingUser.setAppUsername(appUserDTO.getUsername());
        existingUser.setValidated(appUserDTO.isValidated());
        existingUser.setPremium(appUserDTO.isPremium());
        existingUser.setRole(appUserDTO.getRole());

        AppUser updatedUser = appUserRepository.save(existingUser);
        logger.info("User with ID {} updated successfully.", updatedUser.getId());
        return AppUserDTO.fromEntity(updatedUser);
    }

    @Transactional
    public AppUserDTO updateValidationStatusById(Long id, boolean validated) {
        AppUser appUser = appUserRepository.findById(id).orElseThrow(() -> {
            logger.warn("Attempt to update validation status of non-existent user with ID: {}", id);
            return new ResourceNotFoundException("User not found with ID: " + id);
        });
        appUser.setValidated(validated);
        AppUser updatedUser = appUserRepository.save(appUser);
        logger.info("Validation status for user ID {} updated to {}.", id, validated);
        return AppUserDTO.fromEntity(updatedUser);
    }

    @Transactional
    public AppUserDTO updatePremiumStatusById(Long id, boolean premium) {
        AppUser appUser = appUserRepository.findById(id).orElseThrow(() -> {
            logger.warn("Attempt to update premium status of non-existent user with ID: {}", id);
            return new ResourceNotFoundException("User not found with ID: " + id);
        });
        appUser.setPremium(premium);
        AppUser updatedUser = appUserRepository.save(appUser);
        logger.info("Premium status for user ID {} updated to {}.", id, premium);
        return AppUserDTO.fromEntity(updatedUser);
    }

    public boolean isPremium(String email) {
        AppUser appUser = appUserRepository.findByEmail(email).orElseThrow(() -> {
            logger.warn("Attempt to check premium status of non-existent user with email: {}", email);
            return new ResourceNotFoundException("User not found with email: " + email);
        });
        return appUser.isPremium();
    }

    public boolean isValidated(String email) {
        AppUser appUser = appUserRepository.findByEmail(email).orElseThrow(() -> {
            logger.warn("Attempt to check validation status of non-existent user with email: {}", email);
            return new ResourceNotFoundException("User not found with email: " + email);
        });
        return appUser.isValidated();
    }
}