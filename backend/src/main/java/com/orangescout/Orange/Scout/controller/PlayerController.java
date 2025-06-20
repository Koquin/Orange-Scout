package com.orangescout.Orange.Scout.controller;

import com.orangescout.Orange.Scout.dto.PlayerDTO;
import com.orangescout.Orange.Scout.exception.BadRequestException;
import com.orangescout.Orange.Scout.service.PlayerService;
import com.orangescout.Orange.Scout.service.TeamService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/players")
public class PlayerController {

    private static final Logger logger = LoggerFactory.getLogger(PlayerController.class);

    private final PlayerService playerService;
    private final TeamService teamService;

    public PlayerController(PlayerService playerService, TeamService teamService) {
        this.playerService = playerService;
        this.teamService = teamService;
    }

    @GetMapping
    public ResponseEntity<List<PlayerDTO>> getAllPlayers() {
        logger.info("Request to fetch all players (ADMIN ONLY).");
        List<PlayerDTO> players = playerService.getAllPlayers();
        logger.debug("Returning {} players.", players.size());
        return ResponseEntity.ok(players);
    }

    @PostMapping("/team/{teamId}")
    public ResponseEntity<PlayerDTO> createPlayer(@Valid @RequestBody PlayerDTO playerDTO, @PathVariable Long teamId) { // Standardization: @Valid
        logger.info("Request to create player for team with ID: {}", teamId);

        if (playerDTO.getId_player() != null) {
            logger.warn("Attempt to create player with ID already provided: {}", playerDTO.getId_player());
            throw new BadRequestException("Player ID should not be provided when creating a new player.");
        }

        PlayerDTO savedPlayer = playerService.savePlayer(playerDTO, teamId);
        logger.info("Player with ID {} created successfully for team ID {}.", savedPlayer.getId_player(), teamId);
        return new ResponseEntity<>(savedPlayer, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlayerDTO> updatePlayer(@Valid @RequestBody PlayerDTO playerDTO, @PathVariable Long id) { // Standardization: @Valid
        logger.info("Request to update player with ID: {}", id);

        if (playerDTO.getId_player() == null || !playerDTO.getId_player().equals(id)) {
            logger.warn("ID inconsistency: Path ID {} vs Body ID {}", id, playerDTO.getId_player());
            throw new BadRequestException("The ID in the request body must match the ID in the URL.");
        }

        PlayerDTO updatedPlayer = playerService.updatePlayer(id, playerDTO);
        logger.info("Player with ID {} updated successfully.", updatedPlayer.getId_player());
        return ResponseEntity.ok(updatedPlayer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable Long id) {
        logger.info("Request to delete player with ID: {}", id);
        playerService.deletePlayerById(id);
        logger.info("Player with ID {} deleted successfully.", id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<PlayerDTO>> getPlayersByTeam(@PathVariable Long teamId) {
        logger.info("Request to fetch players for team with ID: {}", teamId);
        List<PlayerDTO> players = playerService.getPlayersByTeamId(teamId);
        logger.debug("Returning {} players for team ID {}.", players.size(), teamId);
        return ResponseEntity.ok(players);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayerDTO> getPlayerById(@PathVariable Long id) {
        logger.info("Request to fetch player by ID: {}", id);
        PlayerDTO playerDTO = playerService.getPlayerById(id);
        logger.debug("Player with ID {} found: {}", id, playerDTO.getId_player());
        return ResponseEntity.ok(playerDTO);
    }
}