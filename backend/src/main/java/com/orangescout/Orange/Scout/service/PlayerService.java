package com.orangescout.Orange.Scout.service;

import com.orangescout.Orange.Scout.dto.PlayerDTO;
import com.orangescout.Orange.Scout.exception.ResourceNotFoundException;
import com.orangescout.Orange.Scout.model.Player;
import com.orangescout.Orange.Scout.model.Team;
import com.orangescout.Orange.Scout.repository.PlayerRepository;
import com.orangescout.Orange.Scout.repository.TeamRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PlayerService {

    private static final Logger logger = LoggerFactory.getLogger(PlayerService.class);

    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;

    public PlayerService(PlayerRepository playerRepository, TeamRepository teamRepository) {
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
    }

    public List<PlayerDTO> getPlayersByTeamId(Long teamId) {
        logger.info("Fetching players for team with ID: {}", teamId);
        if (!teamRepository.existsById(teamId)) {
            logger.warn("Team not found with ID: {}", teamId);
            throw new ResourceNotFoundException("Team not found with ID: " + teamId);
        }
        return playerRepository.findAllByTeamId(teamId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public PlayerDTO getPlayerById(Long id) {
        logger.info("Fetching player with ID: {}", id);
        return playerRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> {
                    logger.warn("Player not found with ID: {}", id);
                    return new ResourceNotFoundException("Player not found with ID: " + id);
                });
    }

    public List<PlayerDTO> getAllPlayers() {
        logger.info("Fetching all players.");
        return playerRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public PlayerDTO savePlayer(PlayerDTO playerDTO, Long teamId) {
        logger.info("Saving new player for team with ID: {}", teamId);
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> {
                    logger.warn("Attempt to save player to non-existent team with ID: {}", teamId);
                    return new ResourceNotFoundException("Team not found with ID: " + teamId);
                });

        Player player = convertToEntity(playerDTO, team);
        Player savedPlayer = playerRepository.save(player);
        logger.info("Player with ID {} saved successfully.", savedPlayer.getId());
        return convertToDTO(savedPlayer);
    }

    @Transactional
    public PlayerDTO updatePlayer(Long id, PlayerDTO playerDTO) {
        logger.info("Updating player with ID: {}", id);
        Player existingPlayer = playerRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Attempt to update non-existent player with ID: {}", id);
                    return new ResourceNotFoundException("Player not found with ID: " + id);
                });

        existingPlayer.setPlayerName(playerDTO.getPlayerName());
        existingPlayer.setJerseyNumber(playerDTO.getJerseyNumber());

        Player updatedPlayer = playerRepository.save(existingPlayer);
        logger.info("Player with ID {} updated successfully.", updatedPlayer.getId());
        return convertToDTO(updatedPlayer);
    }

    @Transactional
    public void deletePlayerById(Long id) {
        if (!playerRepository.existsById(id)) {
            logger.warn("Attempt to delete non-existent player with ID: {}", id);
            throw new ResourceNotFoundException("Player not found with ID: " + id);
        }
        playerRepository.deleteById(id);
        logger.info("Player with ID {} deleted successfully.", id);
    }

    private PlayerDTO convertToDTO(Player player) {
        PlayerDTO playerDTO = new PlayerDTO();
        playerDTO.setId_player(player.getId());
        playerDTO.setPlayerName(player.getPlayerName());
        playerDTO.setJerseyNumber(player.getJerseyNumber());
        return playerDTO;
    }

    private Player convertToEntity(PlayerDTO playerDTO, Team team) {
        Player player = new Player();
        player.setPlayerName(playerDTO.getPlayerName());
        player.setJerseyNumber(playerDTO.getJerseyNumber());
        player.setTeam(team);
        return player;
    }
}