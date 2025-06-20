package com.orangescout.Orange.Scout.service;

import com.orangescout.Orange.Scout.dto.StatsDTO;
import com.orangescout.Orange.Scout.exception.ResourceNotFoundException;
import com.orangescout.Orange.Scout.model.Match;
import com.orangescout.Orange.Scout.model.Player;
import com.orangescout.Orange.Scout.model.Stats;
import com.orangescout.Orange.Scout.repository.MatchRepository;
import com.orangescout.Orange.Scout.repository.PlayerRepository;
import com.orangescout.Orange.Scout.repository.StatsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class StatsService {

    private static final Logger logger = LoggerFactory.getLogger(StatsService.class);

    private final StatsRepository statsRepository;
    private final MatchRepository matchRepository;
    private final PlayerRepository playerRepository;

    public StatsService(StatsRepository statsRepository, MatchRepository matchRepository, PlayerRepository playerRepository) {
        this.statsRepository = statsRepository;
        this.matchRepository = matchRepository;
        this.playerRepository = playerRepository;
    }

    @Transactional
    public StatsDTO saveOrUpdateStats(StatsDTO statsDTO) {
        logger.info("Processing statistics for player ID: {} in match ID: {}.", statsDTO.getPlayerId(), statsDTO.getMatchId());

        Match match = matchRepository.findById(statsDTO.getMatchId())
                .orElseThrow(() -> {
                    logger.warn("Match not found with ID: {} for saving statistics.", statsDTO.getMatchId());
                    return new ResourceNotFoundException("Match not found with ID: " + statsDTO.getMatchId());
                });

        Player player = playerRepository.findById(statsDTO.getPlayerId())
                .orElseThrow(() -> {
                    logger.warn("Player not found with ID: {} for saving statistics.", statsDTO.getPlayerId());
                    return new ResourceNotFoundException("Player not found with ID: " + statsDTO.getPlayerId());
                });

        Stats stats;
        Optional<Stats> existingStats = statsRepository.findByMatchIdAndPlayerId(match.getId(), player.getId());

        if (existingStats.isPresent()) {
            stats = existingStats.get();
            logger.debug("Updating existing statistics (ID: {}).", stats.getId());
        } else {
            stats = new Stats();
            stats.setMatch(match);
            stats.setPlayer(player);
            stats.setTeam(player.getTeam());
            logger.debug("Creating new statistics.");
        }

        stats.setThreePointers(statsDTO.getThreePointers());
        stats.setTwoPointers(statsDTO.getTwoPointers());
        stats.setOnePointers(statsDTO.getOnePointers());
        stats.setMissedThreePointers(statsDTO.getMissedThreePointers());
        stats.setMissedTwoPointers(statsDTO.getMissedTwoPointers());
        stats.setMissedOnePointers(statsDTO.getMissedOnePointers());
        stats.setSteals(statsDTO.getSteals());
        stats.setTurnovers(statsDTO.getTurnovers());
        stats.setBlocks(statsDTO.getBlocks());
        stats.setAssists(statsDTO.getAssists());
        stats.setOffensiveRebounds(statsDTO.getOffensiveRebounds());
        stats.setDefensiveRebounds(statsDTO.getDefensiveRebounds());
        stats.setFouls(statsDTO.getFouls());

        Stats savedStats = statsRepository.save(stats);
        logger.info("Statistics (ID: {}) saved/updated successfully.", savedStats.getId());
        return convertToDTO(savedStats);
    }

    public List<StatsDTO> getStatsByMatchId(Long matchId) {
        logger.info("Fetching statistics for match with ID: {}", matchId);
        if (!matchRepository.existsById(matchId)) {
            logger.warn("Match not found with ID: {} for fetching statistics.", matchId);
            throw new ResourceNotFoundException("Match not found with ID: " + matchId);
        }
        return statsRepository.findByMatchId(matchId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public StatsDTO getStatsById(Long id) {
        logger.info("Fetching statistic with ID: {}", id);
        return statsRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> {
                    logger.warn("Statistic not found with ID: {}", id);
                    return new ResourceNotFoundException("Statistic not found with ID: " + id);
                });
    }

    public List<StatsDTO> getAllStats() {
        logger.info("Fetching all statistics.");
        return statsRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteStatsById(Long id) {
        if (!statsRepository.existsById(id)) {
            logger.warn("Attempt to delete non-existent statistic with ID: {}", id);
            throw new ResourceNotFoundException("Statistic not found with ID: " + id);
        }
        statsRepository.deleteById(id);
        logger.info("Statistic with ID {} deleted successfully.", id);
    }

    private StatsDTO convertToDTO(Stats stats) {
        StatsDTO statsDto = new StatsDTO();
        statsDto.setStatsId(stats.getId());
        statsDto.setMatchId(stats.getMatch() != null ? stats.getMatch().getId() : null);
        statsDto.setPlayerId(stats.getPlayer() != null ? stats.getPlayer().getId() : null);
        statsDto.setTeamName(stats.getTeam() != null ? stats.getTeam().getName() : null);
        statsDto.setPlayerJersey(stats.getPlayer() != null ? stats.getPlayer().getJerseyNumber() : null);

        statsDto.setThreePointers(stats.getThreePointers());
        statsDto.setTwoPointers(stats.getTwoPointers());
        statsDto.setOnePointers(stats.getOnePointers());
        statsDto.setMissedThreePointers(stats.getMissedThreePointers());
        statsDto.setMissedTwoPointers(stats.getMissedTwoPointers());
        statsDto.setMissedOnePointers(stats.getMissedOnePointers());
        statsDto.setSteals(stats.getSteals());
        statsDto.setTurnovers(stats.getTurnovers());
        statsDto.setBlocks(stats.getBlocks());
        statsDto.setAssists(stats.getAssists());
        statsDto.setOffensiveRebounds(stats.getOffensiveRebounds());
        statsDto.setDefensiveRebounds(stats.getDefensiveRebounds());
        statsDto.setFouls(stats.getFouls());

        return statsDto;
    }
}