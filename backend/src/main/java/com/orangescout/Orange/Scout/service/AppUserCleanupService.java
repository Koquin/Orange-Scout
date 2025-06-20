package com.orangescout.Orange.Scout.service;

import com.orangescout.Orange.Scout.repository.AppUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AppUserCleanupService {

    private static final Logger logger = LoggerFactory.getLogger(AppUserCleanupService.class);

    private final AppUserRepository appUserRepository;

    public AppUserCleanupService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Scheduled(fixedRate = 3600000)
    @Transactional
    public void cleanupUnvalidatedAppUsers() {
        logger.info("Starting scheduled task: Cleaning up non-validated AppUser accounts...");

        int deletedCount = appUserRepository.deleteByValidatedFalseAndCreatedAtBefore(
                LocalDateTime.now().minusHours(1)
        );

        logger.info("Cleanup task completed. Total accounts removed: {}", deletedCount);
    }
}