package com.orangescout.Orange.Scout.service;

import com.orangescout.Orange.Scout.dto.TeamDTO;
import com.orangescout.Orange.Scout.exception.ResourceNotFoundException;
import com.orangescout.Orange.Scout.model.AppUser;
import com.orangescout.Orange.Scout.model.Team;
import com.orangescout.Orange.Scout.repository.AppUserRepository;
import com.orangescout.Orange.Scout.repository.TeamRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TeamService {

    private static final Logger logger = LoggerFactory.getLogger(TeamService.class);

    private final TeamRepository teamRepository;
    private final AppUserRepository appUserRepository;

    public TeamService(TeamRepository teamRepository, AppUserRepository appUserRepository) {
        this.teamRepository = teamRepository;
        this.appUserRepository = appUserRepository;
    }

    public List<TeamDTO> getAllTeams() {
        logger.info("Fetching all teams.");
        return teamRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<TeamDTO> getAllTeamsByUserId(Long userId) {
        logger.info("Fetching teams for user with ID: {}", userId);
        AppUser appUser = appUserRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.warn("User not found with ID: {} when fetching teams.", userId);
                    return new ResourceNotFoundException("User not found with ID: " + userId);
                });
        return teamRepository.findByUser(appUser).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<TeamDTO> getAllTeamsByUserEmail(String email) {
        logger.info("Fetching teams for user with email: {}", email);
        AppUser appUser = appUserRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.warn("User not found with email: {} when fetching teams.", email);
                    return new ResourceNotFoundException("User not found with email: " + email);
                });
        return teamRepository.findByUser(appUser).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public TeamDTO getTeamById(Long id) {
        logger.info("Fetching team with ID: {}", id);
        return teamRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> {
                    logger.warn("Team not found with ID: {}", id);
                    return new ResourceNotFoundException("Team not found with ID: " + id);
                });
    }

    @Transactional
    public void deleteTeamById(Long id) {
        if (!teamRepository.existsById(id)) {
            logger.warn("Attempt to delete non-existent team with ID: {}", id);
            throw new ResourceNotFoundException("Team not found with ID: " + id);
        }
        teamRepository.deleteById(id);
        logger.info("Team with ID {} deleted successfully.", id);
    }

    @Transactional
    public TeamDTO saveOrUpdateTeam(TeamDTO teamDTO, Long userId) {
        AppUser appUser = appUserRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.warn("User not found with ID: {} when saving/updating team.", userId);
                    return new ResourceNotFoundException("User not found with ID: " + userId);
                });

        Team team;
        if (teamDTO.getId() != null) {
            team = teamRepository.findById(teamDTO.getId())
                    .orElseThrow(() -> {
                        logger.warn("Attempt to update team, but ID not found: {}", teamDTO.getId());
                        return new ResourceNotFoundException("Team not found with ID: " + teamDTO.getId());
                    });
            logger.info("Updating existing team with ID: {}", team.getId());
        } else {
            team = new Team();
            team.setAppUser(appUser);
            logger.info("Creating new team for user with ID: {}", userId);
        }

        team.setName(teamDTO.getTeamName());
        team.setAbbr(teamDTO.getAbbreviation());
        team.setLogoPath(teamDTO.getLogoPath());

        Team savedTeam = teamRepository.save(team);
        logger.info("Team with ID {} saved/updated successfully.", savedTeam.getId());
        return convertToDTO(savedTeam);
    }

    private TeamDTO convertToDTO(Team team) {
        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setId(team.getId());
        teamDTO.setTeamName(team.getName());
        teamDTO.setLogoPath(team.getLogoPath());
        teamDTO.setAbbreviation(team.getAbbr());
        teamDTO.setUserId(team.getAppUser() != null ? team.getAppUser().getId() : null);
        return teamDTO;
    }
}