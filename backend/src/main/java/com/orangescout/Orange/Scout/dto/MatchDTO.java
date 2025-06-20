package com.orangescout.Orange.Scout.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

public class MatchDTO {

    private Long idMatch;

    private Long appUserId;

    @NotNull(message = "O modo de jogo não pode ser nulo.")
    @NotBlank(message = "O modo de jogo não pode estar em branco.")
    @Size(min = 2, max = 50, message = "O modo de jogo deve ter entre 2 e 50 caracteres.")
    private String gameMode;

    @NotNull(message = "A data da partida não pode ser nula.")
    private LocalDate matchDate;

    @Min(value = 0, message = "O placar do Time Um não pode ser negativo.")
    private int teamOneScore;

    @Min(value = 0, message = "O placar do Time Dois não pode ser negativo.")
    private int teamTwoScore;

    @NotNull(message = "O Time Um (da casa) não pode ser nulo.")
    @Valid
    private TeamDTO teamOne;

    @NotNull(message = "O Time Dois (visitante) não pode ser nulo.")
    @Valid
    private TeamDTO teamTwo;

    @NotNull(message = "A lista de estatísticas não pode ser nula.")
    @Valid
    private List<StatsDTO> stats;

    @Valid
    private LocationDTO location;

    @NotNull(message = "O status 'finalizada' não pode ser nulo.")
    private boolean finished;

    @NotNull(message = "A lista de titulares do Time 1 não pode ser nula.")
    @Valid
    private List<PlayerDTO> startersTeam1;

    @NotNull(message = "A lista de titulares do Time 2 não pode ser nula.")
    @Valid
    private List<PlayerDTO> startersTeam2;

    public MatchDTO() {
    }

    public MatchDTO(Long idMatch, String gameMode, LocalDate matchDate, int teamOneScore, int teamTwoScore,
                    TeamDTO teamOne, TeamDTO teamTwo, List<StatsDTO> stats, LocationDTO location,
                    boolean finished, List<PlayerDTO> startersTeam1, List<PlayerDTO> startersTeam2, Long appUserId) {
        this.appUserId = appUserId;
        this.idMatch = idMatch;
        this.gameMode = gameMode;
        this.matchDate = matchDate;
        this.teamOneScore = teamOneScore;
        this.teamTwoScore = teamTwoScore;
        this.teamOne = teamOne;
        this.teamTwo = teamTwo;
        this.stats = stats;
        this.location = location;
        this.finished = finished;
        this.startersTeam1 = startersTeam1;
        this.startersTeam2 = startersTeam2;
    }

    public Long getIdMatch() {
        return idMatch;
    }

    public void setIdMatch(Long idMatch) {
        this.idMatch = idMatch;
    }

    public String getGameMode() {
        return gameMode;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    public LocalDate getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(LocalDate matchDate) {
        this.matchDate = matchDate;
    }

    public int getTeamOneScore() {
        return teamOneScore;
    }

    public void setTeamOneScore(int teamOneScore) {
        this.teamOneScore = teamOneScore;
    }

    public int getTeamTwoScore() {
        return teamTwoScore;
    }

    public void setTeamTwoScore(int teamTwoScore) {
        this.teamTwoScore = teamTwoScore;
    }

    public TeamDTO getTeamOne() {
        return teamOne;
    }

    public void setTeamOne(TeamDTO teamOne) {
        this.teamOne = teamOne;
    }

    public TeamDTO getTeamTwo() {
        return teamTwo;
    }

    public void setTeamTwo(TeamDTO teamTwo) {
        this.teamTwo = teamTwo;
    }

    public List<StatsDTO> getStats() {
        return stats;
    }

    public void setStats(List<StatsDTO> stats) {
        this.stats = stats;
    }

    public LocationDTO getLocation() { // PADRONIZAÇÃO: Getter retorna LocationDTO
        return location;
    }

    public void setLocation(LocationDTO location) { // PADRONIZAÇÃO: Setter recebe LocationDTO
        this.location = location;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public List<PlayerDTO> getStartersTeam1() {
        return startersTeam1;
    }

    public void setStartersTeam1(List<PlayerDTO> startersTeam1) {
        this.startersTeam1 = startersTeam1;
    }

    public List<PlayerDTO> getStartersTeam2() {
        return startersTeam2;
    }

    public void setStartersTeam2(List<PlayerDTO> startersTeam2) {
        this.startersTeam2 = startersTeam2;
    }

    public Long getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(Long appUserId) {
        this.appUserId = appUserId;
    }

    @Override
    public String toString() {
        return "MatchDTO{" +
                "idMatch=" + idMatch +
                ", appUserId='" + appUserId +
                ", gameMode='" + gameMode + '\'' +
                ", matchDate=" + matchDate +
                ", teamOneScore=" + teamOneScore +
                ", teamTwoScore=" + teamTwoScore +
                ", teamOne=" + teamOne +
                ", teamTwo=" + teamTwo +
                ", stats=" + (stats != null ? stats.size() + " items" : "null") + // Evitar toString de lista grande
                ", location=" + location +
                ", finished=" + finished +
                ", startersTeam1=" + (startersTeam1 != null ? startersTeam1.size() + " items" : "null") +
                ", startersTeam2=" + (startersTeam2 != null ? startersTeam2.size() + " items" : "null") +
                '}';
    }
}