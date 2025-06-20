package com.orangescout.Orange.Scout.controller;

import com.orangescout.Orange.Scout.dto.StatsDTO;
import com.orangescout.Orange.Scout.service.StatsService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stats")
public class StatsController {

    private static final Logger logger = LoggerFactory.getLogger(StatsController.class);

    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("/match/{matchId}")
    public ResponseEntity<List<StatsDTO>> getStatsByMatchId(@PathVariable Long matchId) {
        logger.info("Request to fetch statistics for match with ID: {}", matchId);
        List<StatsDTO> stats = statsService.getStatsByMatchId(matchId);
        logger.debug("Returning {} statistics for match ID {}.", stats.size(), matchId);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StatsDTO> getStatsById(@PathVariable Long id) {
        logger.info("Request to fetch statistic with ID: {}", id);
        StatsDTO statsDTO = statsService.getStatsById(id);
        logger.debug("Statistic with ID {} found: {}", id, statsDTO.getStatsId());
        return ResponseEntity.ok(statsDTO);
    }

    @GetMapping
    public ResponseEntity<List<StatsDTO>> getAllStats() {
        logger.info("Request to fetch all statistics.");
        List<StatsDTO> allStats = statsService.getAllStats();
        logger.debug("Returning {} total statistics.", allStats.size());
        return ResponseEntity.ok(allStats);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStats(@PathVariable Long id) {
        logger.info("Request to delete statistic with ID: {}", id);
        statsService.deleteStatsById(id);
        logger.info("Statistic with ID {} deleted successfully.", id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<StatsDTO> saveOrUpdateStats(@Valid @RequestBody StatsDTO statsDTO) {
        logger.info("Request to save or update statistic for player ID: {} in match ID: {}.",
                statsDTO.getPlayerId(), statsDTO.getMatchId());

        StatsDTO savedStats = statsService.saveOrUpdateStats(statsDTO);

        HttpStatus status = (statsDTO.getStatsId() == null) ? HttpStatus.CREATED : HttpStatus.OK;
        logger.info("Statistic with ID {} processed successfully. Status: {}", savedStats.getStatsId(), status);
        return new ResponseEntity<>(savedStats, status);
    }
}