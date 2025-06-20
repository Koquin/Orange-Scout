package com.orangescout.Orange.Scout.controller;

import com.orangescout.Orange.Scout.dto.TeamDTO;
import com.orangescout.Orange.Scout.service.AppUserService;
import com.orangescout.Orange.Scout.service.TeamService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teams")
public class TeamController {

    private static final Logger logger = LoggerFactory.getLogger(TeamController.class);

    private final TeamService teamService;
    private final AppUserService appUserService;

    public TeamController(TeamService teamService, AppUserService appUserService) {
        this.teamService = teamService;
        this.appUserService = appUserService;
    }

    @GetMapping
    public ResponseEntity<List<TeamDTO>> getAllTeamsByAppUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        logger.info("Request to fetch all user teams: {}", userEmail);
        List<TeamDTO> teams = teamService.getAllTeamsByUserEmail(userEmail);
        logger.debug("Returning {} teams for user {}.", teams.size(), userEmail);
        return ResponseEntity.ok(teams);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamDTO> getTeamById(@PathVariable Long id) {
        logger.info("Request to fetch team with ID: {}", id);
        TeamDTO teamDTO = teamService.getTeamById(id);
        logger.debug("Team with ID {} found: {}", id, teamDTO.getId());
        return ResponseEntity.ok(teamDTO);
    }

    @PostMapping
    public ResponseEntity<TeamDTO> createOrUpdateTeam(@Valid @RequestBody TeamDTO teamDTO) {
        logger.info("Request to create/update team: {}", teamDTO.getTeamName());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        Long userId = appUserService.getAppUserByEmail(userEmail).getId();
        logger.info("User ID found: '{}' with email: '{}' ", userId, userEmail);

        TeamDTO savedTeam = teamService.saveOrUpdateTeam(teamDTO, userId);
        HttpStatus status = (teamDTO.getId() == null) ? HttpStatus.CREATED : HttpStatus.OK;
        logger.info("Team with ID {} successfully processed. Status: {}", savedTeam.getId(), status);
        return new ResponseEntity<>(savedTeam, status);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long id) {
        logger.info("Request to delete team with ID: {}", id);
        teamService.deleteTeamById(id);
        logger.info("Team with ID {} deleted successfully.", id);
        return ResponseEntity.noContent().build();
    }
}