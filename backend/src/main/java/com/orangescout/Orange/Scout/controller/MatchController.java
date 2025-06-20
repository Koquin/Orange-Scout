package com.orangescout.Orange.Scout.controller;

import com.orangescout.Orange.Scout.dto.MatchDTO;
import com.orangescout.Orange.Scout.dto.TeamDTO;
import com.orangescout.Orange.Scout.model.AppUser;
import com.orangescout.Orange.Scout.service.AppUserService;
import com.orangescout.Orange.Scout.service.MatchService;
import com.orangescout.Orange.Scout.service.TeamService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/matches")
public class MatchController {

    private static final Logger logger = LoggerFactory.getLogger(MatchController.class);

    private final MatchService matchService;
    private final AppUserService appUserService;
    private final TeamService teamService;

    public MatchController(MatchService matchService, AppUserService appUserService, TeamService teamService) {
        this.matchService = matchService;
        this.appUserService = appUserService;
        this.teamService = teamService;
    }

    @GetMapping
    public ResponseEntity<List<MatchDTO>> getAllMatches() {
        logger.info("Request to fetch all matches.");
        List<MatchDTO> matches = matchService.getAllMatches();
        logger.debug("Returning {} matches.", matches.size());
        return ResponseEntity.ok(matches);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatchDTO> getMatchById(@PathVariable Long id) {
        logger.info("Request to fetch match with ID: {}", id);
        MatchDTO matchDTO = matchService.getMatchById(id);
        logger.debug("Match with ID {} found: {}", id, matchDTO.getIdMatch());
        return ResponseEntity.ok(matchDTO);
    }

    @PostMapping
    public ResponseEntity<Map<String, Long>> processMatch(@Valid @RequestBody MatchDTO matchDTO) {
        logger.info("Request to process match. Is it a new match? {}", matchDTO.getIdMatch() == null);
        Long matchId = matchService.processMatchWithStats(matchDTO);

        HttpStatus status = (matchDTO.getIdMatch() == null) ? HttpStatus.CREATED : HttpStatus.OK;
        logger.info("Match with ID {} processed successfully. Status: {}", matchId, status);
        return new ResponseEntity<>(Collections.singletonMap("matchId", matchId), status);
    }

    @GetMapping("/user-history")
    public ResponseEntity<List<MatchDTO>> getUserMatchesHistory() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        AppUser currentUser = appUserService.getAppUserById(appUserService.getAppUserByEmail(userEmail).getId()); // Get authenticated user's ID
        logger.info("Request to fetch user match history for: {}", userEmail);

        List<MatchDTO> matches = matchService.getMatchesByUserId(currentUser.getId());
        logger.debug("Returning {} matches for user history {}.", matches.size(), userEmail);
        return ResponseEntity.ok(matches);
    }

    @GetMapping("/last-unfinished")
    public ResponseEntity<MatchDTO> getLastUnfinishedMatch() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        AppUser currentUser = appUserService.getAppUserById(appUserService.getAppUserByEmail(userEmail).getId());
        logger.info("Request to fetch last unfinished match for user: {}", userEmail);

        MatchDTO lastMatchDTO = matchService.findLastUnfinishedMatchByUserId(currentUser.getId());

        if (lastMatchDTO != null) {
            logger.debug("Last unfinished match found for {}: ID {}", userEmail, lastMatchDTO.getIdMatch());
            return ResponseEntity.ok(lastMatchDTO);
        } else {
            logger.info("No unfinished match found for user: {}", userEmail);
            return ResponseEntity.noContent().build();
        }
    }

    @PutMapping("/finish/{id}")
    public ResponseEntity<Void> finishMatch(@PathVariable Long id) {
        logger.info("Request to finish match with ID: {}", id);
        matchService.finishMatch(id);
        logger.info("Match with ID {} finished successfully.", id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/validate-start")
    public ResponseEntity<Boolean> validateStartGame() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        logger.info("Request to validate game start for user: {}", userEmail);

        List<TeamDTO> teams = teamService.getAllTeamsByUserEmail(userEmail);
        boolean canStart = teams.size() >= 2;
        logger.debug("User {} has {} teams. Can start game? {}", userEmail, teams.size(), canStart);
        return ResponseEntity.ok(canStart);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMatch(@PathVariable Long id) {
        logger.info("Request to delete match with ID: {}", id);
        matchService.deleteMatchById(id);
        logger.info("Match with ID {} deleted successfully.", id);
        return ResponseEntity.noContent().build();
    }
}