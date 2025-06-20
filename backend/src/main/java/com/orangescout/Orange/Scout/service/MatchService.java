package com.orangescout.Orange.Scout.service;

import com.orangescout.Orange.Scout.dto.LocationDTO;
import com.orangescout.Orange.Scout.dto.MatchDTO;
import com.orangescout.Orange.Scout.dto.PlayerDTO;
import com.orangescout.Orange.Scout.dto.StatsDTO;
import com.orangescout.Orange.Scout.dto.TeamDTO;
import com.orangescout.Orange.Scout.exception.ResourceNotFoundException;
import com.orangescout.Orange.Scout.model.AppUser;
import com.orangescout.Orange.Scout.model.Location;
import com.orangescout.Orange.Scout.model.Match;
import com.orangescout.Orange.Scout.model.Player;
import com.orangescout.Orange.Scout.model.StartersTeamOne;
import com.orangescout.Orange.Scout.model.StartersTeamTwo;
import com.orangescout.Orange.Scout.model.Stats;
import com.orangescout.Orange.Scout.model.Team;
import com.orangescout.Orange.Scout.repository.AppUserRepository;
import com.orangescout.Orange.Scout.repository.LocationRepository;
import com.orangescout.Orange.Scout.repository.MatchRepository;
import com.orangescout.Orange.Scout.repository.PlayerRepository;
import com.orangescout.Orange.Scout.repository.StartersTeamOneRepository;
import com.orangescout.Orange.Scout.repository.StartersTeamTwoRepository;
import com.orangescout.Orange.Scout.repository.StatsRepository;
import com.orangescout.Orange.Scout.repository.TeamRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MatchService {

    private static final Logger logger = LoggerFactory.getLogger(MatchService.class);

    private final MatchRepository matchRepository;
    private final AppUserService appUserService;
    private final TeamRepository teamRepository;
    private final AppUserRepository appUserRepository;
    private final PlayerRepository playerRepository;
    private final StatsRepository statsRepository;
    private final LocationRepository locationRepository;
    private final StartersTeamOneRepository startersTeamOneRepository;
    private final StartersTeamTwoRepository startersTeamTwoRepository;

    public MatchService(MatchRepository matchRepository, AppUserService appUserService, TeamRepository teamRepository,
                        AppUserRepository appUserRepository, PlayerRepository playerRepository, StatsRepository statsRepository,
                        LocationRepository locationRepository, StartersTeamOneRepository startersTeamOneRepository,
                        StartersTeamTwoRepository startersTeamTwoRepository) {
        this.matchRepository = matchRepository;
        this.appUserService = appUserService;
        this.teamRepository = teamRepository;
        this.appUserRepository = appUserRepository;
        this.playerRepository = playerRepository;
        this.statsRepository = statsRepository;
        this.locationRepository = locationRepository;
        this.startersTeamOneRepository = startersTeamOneRepository;
        this.startersTeamTwoRepository = startersTeamTwoRepository;
    }

    public List<MatchDTO> getAllMatches() {
        logger.info("Fetching all matches.");
        return matchRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public MatchDTO getMatchById(Long id) {
        logger.info("Fetching match with ID: {}", id);
        return matchRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> {
                    logger.warn("Match not found with ID: {}", id);
                    return new ResourceNotFoundException("Match not found with ID: " + id);
                });
    }

    @Transactional
    public void deleteMatchById(Long id) {
        if (!matchRepository.existsById(id)) {
            logger.warn("Attempt to delete non-existent match with ID: {}", id);
            throw new ResourceNotFoundException("Match not found with ID: " + id);
        }
        matchRepository.deleteById(id);
        logger.info("Match with ID {} deleted successfully.", id);
    }

    @Transactional
    public Long processMatchWithStats(MatchDTO matchDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        AppUser currentUser = appUserRepository.findByEmail(userEmail)
                .orElseThrow(() -> {
                    logger.error("Authenticated user not found in database: {}", userEmail);
                    return new ResourceNotFoundException("Authenticated user not found.");
                });

        Match match;
        boolean isUpdating = matchDTO.getIdMatch() != null;
        logger.info("Processing match. Is it an update? {}. Match ID in DTO: {}", isUpdating, matchDTO.getIdMatch());

        if (isUpdating) {
            match = matchRepository.findById(matchDTO.getIdMatch())
                    .orElseThrow(() -> {
                        logger.warn("Attempt to update non-existent match, but ID not found: {}", matchDTO.getIdMatch());
                        return new ResourceNotFoundException("Match not found with ID: " + matchDTO.getIdMatch());
                    });
            logger.info("Updating existing match with ID: {}", match.getId());
        } else {
            match = new Match();
            match.setUser(currentUser);
            logger.info("Creating new match for user: {}, with ID: {}", currentUser.getEmail(), currentUser.getId());
        }
        logger.debug("Assigning other attributes besides user.");
        match.setMatchDate(matchDTO.getMatchDate());
        match.setTeamOneScore(matchDTO.getTeamOneScore());
        match.setTeamTwoScore(matchDTO.getTeamTwoScore());
        match.setFinished(matchDTO.isFinished());
        match.setGameMode(matchDTO.getGameMode());
        logger.debug("Date, teams, finished and game mode assigned.");
        Team teamOne = teamRepository.findById(matchDTO.getTeamOne().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Home Team not found with ID: " + matchDTO.getTeamOne().getId()));
        match.setTeamOne(teamOne);

        Team teamTwo = teamRepository.findById(matchDTO.getTeamTwo().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Away Team not found with ID: " + matchDTO.getTeamTwo().getId()));
        match.setTeamTwo(teamTwo);
        logger.debug("All assigned, now saving...");

        match = matchRepository.save(match);
        logger.debug("Base match saved/updated with ID: {}", match.getId());

        if (matchDTO.getLocation() != null) {
            Location location;
            if (match.getLocation() != null && match.getLocation().getId() != null) {
                location = match.getLocation();
                logger.debug("Updating existing location with ID: {}", location.getId());
            } else {
                location = new Location();
                logger.debug("Creating new location for the match.");
            }
            location.setPlaceName(matchDTO.getLocation().getVenueName());
            location.setLatitude(matchDTO.getLocation().getLatitude());
            location.setLongitude(matchDTO.getLocation().getLongitude());
            location = locationRepository.save(location);
            match.setLocation(location);
            logger.debug("Match location (ID: {}) processed: {}", match.getId(), location.getId());
        } else if (match.getLocation() != null) {
            locationRepository.delete(match.getLocation());
            match.setLocation(null);
            logger.info("Match location (ID: {}) removed.", match.getId());
        }

        StartersTeamOne starters1 = match.getStartersTeamOne();
        if (starters1 == null) {
            starters1 = new StartersTeamOne();
            starters1.setMatch(match);
        }
        List<Player> starters1Players = matchDTO.getStartersTeam1().stream()
                .map(dto -> playerRepository.findById(dto.getId_player())
                        .orElseThrow(() -> new ResourceNotFoundException("Starting Player for Team 1 with ID " + dto.getId_player() + " not found.")))
                .collect(Collectors.toList());
        starters1.setStarters(starters1Players);
        startersTeamOneRepository.save(starters1);
        match.setStartersTeamOne(starters1);
        logger.debug("Starting Players for Team 1 processed for match ID: {}", match.getId());

        StartersTeamTwo starters2 = match.getStartersTeamTwo();
        if (starters2 == null) {
            starters2 = new StartersTeamTwo();
            starters2.setMatch(match);
        }
        List<Player> starters2Players = matchDTO.getStartersTeam2().stream()
                .map(dto -> playerRepository.findById(dto.getId_player())
                        .orElseThrow(() -> new ResourceNotFoundException("Starting Player for Team 2 with ID " + dto.getId_player() + " not found.")))
                .collect(Collectors.toList());
        starters2.setStarters(starters2Players);
        startersTeamTwoRepository.save(starters2);
        match.setStartersTeamTwo(starters2);
        logger.debug("Starting Players for Team 2 processed for match ID: {}", match.getId());

        matchRepository.save(match);
        logger.debug("Match (ID: {}) with locations and starters finalized.", match.getId());

        for (StatsDTO statsDTO : matchDTO.getStats()) {
            Stats stats;
            Optional<Stats> existingStatsOpt = statsRepository.findByMatchIdAndPlayerId(match.getId(), statsDTO.getPlayerId());

            if (existingStatsOpt.isPresent()) {
                stats = existingStatsOpt.get();
                logger.debug("Updating existing statistics for player {} in match {}.", statsDTO.getPlayerId(), match.getId());
            } else {
                stats = new Stats();
                stats.setMatch(match);
                Player player = playerRepository.findById(statsDTO.getPlayerId())
                        .orElseThrow(() -> new ResourceNotFoundException("Player with ID " + statsDTO.getPlayerId() + " not found for statistics."));
                stats.setPlayer(player);
                stats.setTeam(player.getTeam());
                logger.debug("Creating new statistics for player {} in match {}.", statsDTO.getPlayerId(), match.getId());
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

            statsRepository.save(stats);
        }
        logger.info("All statistics successfully processed for match ID: {}", match.getId());
        return match.getId();
    }

    public List<MatchDTO> getMatchesByUserId(Long userId) {
        logger.info("Fetching matches for user with ID: {}", userId);
        AppUser appUser = appUserService.getAppUserById(userId);
        List<Match> matches = matchRepository.findByUserAndFinishedTrue(appUser);
        return matches.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public MatchDTO findLastUnfinishedMatchByUserId(Long userId) {
        logger.info("Fetching last unfinished match for user with ID: {}", userId);
        AppUser appUser = appUserService.getAppUserById(userId);
        Optional<Match> lastUnfinishedMatch = matchRepository.findTopByUserAndFinishedFalseOrderByIdDesc(appUser);
        return lastUnfinishedMatch.map(this::convertToDTO).orElse(null);
    }

    @Transactional
    public void finishMatch(Long matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> {
                    logger.warn("Attempt to finish non-existent match with ID: {}", matchId);
                    return new ResourceNotFoundException("Match not found with ID: " + matchId);
                });
        if (match.isFinished()) {
            logger.info("Match with ID {} was already finished.", matchId);
            return;
        }
        match.setFinished(true);
        matchRepository.save(match);
        logger.info("Match with ID {} finished successfully.", matchId);
    }

    private MatchDTO convertToDTO(Match match) {
        MatchDTO matchDTO = new MatchDTO();
        matchDTO.setAppUserId(match.getUser().getId());
        matchDTO.setIdMatch(match.getId());
        matchDTO.setMatchDate(match.getMatchDate());
        matchDTO.setTeamOneScore(match.getTeamOneScore());
        matchDTO.setTeamTwoScore(match.getTeamTwoScore());
        matchDTO.setGameMode(match.getGameMode());
        matchDTO.setFinished(match.isFinished());

        if (match.getLocation() != null) {
            matchDTO.setLocation(convertToLocationDTO(match.getLocation()));
        }

        if (match.getTeamOne() != null) {
            matchDTO.setTeamOne(convertToTeamDTO(match.getTeamOne()));
        }

        if (match.getTeamTwo() != null) {
            matchDTO.setTeamTwo(convertToTeamDTO(match.getTeamTwo()));
        }

        if (match.getStats() != null && !match.getStats().isEmpty()) {
            matchDTO.setStats(match.getStats().stream()
                    .map(this::convertToStatsDTO)
                    .collect(Collectors.toList()));
        }

        if (match.getStartersTeamOne() != null && match.getStartersTeamOne().getStarters() != null) {
            matchDTO.setStartersTeam1(match.getStartersTeamOne().getStarters().stream()
                    .map(this::convertToPlayerDTO)
                    .collect(Collectors.toList()));
        }

        if (match.getStartersTeamTwo() != null && match.getStartersTeamTwo().getStarters() != null) {
            matchDTO.setStartersTeam2(match.getStartersTeamTwo().getStarters().stream()
                    .map(this::convertToPlayerDTO)
                    .collect(Collectors.toList()));
        }
        return matchDTO;
    }

    private LocationDTO convertToLocationDTO(Location location) {
        LocationDTO dto = new LocationDTO();
        dto.setId(location.getId());
        dto.setVenueName(location.getPlaceName());
        dto.setLatitude(location.getLatitude());
        dto.setLongitude(location.getLongitude());
        return dto;
    }

    private TeamDTO convertToTeamDTO(Team team) {
        TeamDTO dto = new TeamDTO();
        dto.setId(team.getId());
        dto.setTeamName(team.getName());
        dto.setLogoPath(team.getLogoPath());
        dto.setAbbreviation(team.getAbbr());
        return dto;
    }

    private PlayerDTO convertToPlayerDTO(Player player) {
        PlayerDTO dto = new PlayerDTO();
        dto.setId_player(player.getId());
        dto.setPlayerName(player.getPlayerName());
        dto.setJerseyNumber(player.getJerseyNumber());
        return dto;
    }

    private StatsDTO convertToStatsDTO(Stats stats) {
        StatsDTO statsDTO = new StatsDTO();
        statsDTO.setStatsId(stats.getId());
        statsDTO.setMatchId(stats.getMatch().getId());
        statsDTO.setPlayerId(stats.getPlayer().getId());
        statsDTO.setTeamName(stats.getTeam().getName());
        statsDTO.setPlayerJersey(stats.getPlayer().getJerseyNumber());
        statsDTO.setThreePointers(stats.getThreePointers());
        statsDTO.setTwoPointers(stats.getTwoPointers());
        statsDTO.setOnePointers(stats.getOnePointers());
        statsDTO.setMissedThreePointers(stats.getMissedThreePointers());
        statsDTO.setMissedTwoPointers(stats.getMissedTwoPointers());
        statsDTO.setMissedOnePointers(stats.getMissedOnePointers());
        statsDTO.setSteals(stats.getSteals());
        statsDTO.setTurnovers(stats.getTurnovers());
        statsDTO.setBlocks(stats.getBlocks());
        statsDTO.setAssists(stats.getAssists());
        statsDTO.setOffensiveRebounds(stats.getOffensiveRebounds());
        statsDTO.setDefensiveRebounds(stats.getDefensiveRebounds());
        statsDTO.setFouls(stats.getFouls());
        return statsDTO;
    }
}