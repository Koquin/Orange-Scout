package com.orangescout.Orange.Scout.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class StatsDTO {

    private Long statsId;

    private Long matchId;

    @NotNull(message = "O ID do jogador não pode ser nulo.")
    private Long playerId;

    @Size(max = 10, message = "O número da camisa do jogador deve ter no máximo 10 caracteres.")
    private String playerJersey;

    private String teamName;

    @Min(value = 0, message = "O número de três pontos não pode ser negativo.")
    private int threePointers;

    @Min(value = 0, message = "O número de dois pontos não pode ser negativo.")
    private int twoPointers;

    @Min(value = 0, message = "O número de um ponto não pode ser negativo.")
    private int onePointers;

    @Min(value = 0, message = "O número de três pontos perdidos não pode ser negativo.")
    private int missedThreePointers;

    @Min(value = 0, message = "O número de dois pontos perdidos não pode ser negativo.")
    private int missedTwoPointers;

    @Min(value = 0, message = "O número de um ponto perdido não pode ser negativo.")
    private int missedOnePointers;

    @Min(value = 0, message = "O número de roubadas de bola não pode ser negativo.")
    private int steals;

    @Min(value = 0, message = "O número de turnovers não pode ser negativo.")
    private int turnovers;

    @Min(value = 0, message = "O número de bloqueios não pode ser negativo.")
    private int blocks;

    @Min(value = 0, message = "O número de assistências não pode ser negativo.")
    private int assists;

    @Min(value = 0, message = "O número de rebotes ofensivos não pode ser negativo.")
    private int offensiveRebounds;

    @Min(value = 0, message = "O número de rebotes defensivos não pode ser negativo.")
    private int defensiveRebounds;

    @Min(value = 0, message = "O número de faltas não pode ser negativo.")
    private int fouls;

    public StatsDTO() {
    }

    public StatsDTO(Long statsId, Long matchId, Long playerId, String playerJersey, String teamName,
                    int threePointers, int twoPointers, int onePointers, int missedThreePointers,
                    int missedTwoPointers, int missedOnePointers, int steals, int turnovers,
                    int blocks, int assists, int offensiveRebounds, int defensiveRebounds, int fouls) {
        this.statsId = statsId;
        this.matchId = matchId;
        this.playerId = playerId;
        this.playerJersey = playerJersey;
        this.teamName = teamName;
        this.threePointers = threePointers;
        this.twoPointers = twoPointers;
        this.onePointers = onePointers;
        this.missedThreePointers = missedThreePointers;
        this.missedTwoPointers = missedTwoPointers;
        this.missedOnePointers = missedOnePointers;
        this.steals = steals;
        this.turnovers = turnovers;
        this.blocks = blocks;
        this.assists = assists;
        this.offensiveRebounds = offensiveRebounds;
        this.defensiveRebounds = defensiveRebounds;
        this.fouls = fouls;
    }


    public Long getStatsId() {
        return statsId;
    }

    public void setStatsId(Long statsId) {
        this.statsId = statsId;
    }

    public Long getMatchId() {
        return matchId;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public String getPlayerJersey() {
        return playerJersey;
    }

    public void setPlayerJersey(String playerJersey) {
        this.playerJersey = playerJersey;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public int getThreePointers() {
        return threePointers;
    }

    public void setThreePointers(int threePointers) {
        this.threePointers = threePointers;
    }

    public int getTwoPointers() {
        return twoPointers;
    }

    public void setTwoPointers(int twoPointers) {
        this.twoPointers = twoPointers;
    }

    public int getOnePointers() {
        return onePointers;
    }

    public void setOnePointers(int onePointers) {
        this.onePointers = onePointers;
    }

    public int getMissedThreePointers() {
        return missedThreePointers;
    }

    public void setMissedThreePointers(int missedThreePointers) {
        this.missedThreePointers = missedThreePointers;
    }

    public int getMissedTwoPointers() {
        return missedTwoPointers;
    }

    public void setMissedTwoPointers(int missedTwoPointers) {
        this.missedTwoPointers = missedTwoPointers;
    }

    public int getMissedOnePointers() {
        return missedOnePointers;
    }

    public void setMissedOnePointers(int missedOnePointers) {
        this.missedOnePointers = missedOnePointers;
    }

    public int getSteals() {
        return steals;
    }

    public void setSteals(int steals) {
        this.steals = steals;
    }

    public int getTurnovers() {
        return turnovers;
    }

    public void setTurnovers(int turnovers) {
        this.turnovers = turnovers;
    }

    public int getBlocks() {
        return blocks;
    }

    public void setBlocks(int blocks) {
        this.blocks = blocks;
    }

    public int getAssists() {
        return assists;
    }

    public void setAssists(int assists) {
        this.assists = assists;
    }

    public int getOffensiveRebounds() {
        return offensiveRebounds;
    }

    public void setOffensiveRebounds(int offensiveRebounds) {
        this.offensiveRebounds = offensiveRebounds;
    }

    public int getDefensiveRebounds() {
        return defensiveRebounds;
    }

    public void setDefensiveRebounds(int defensiveRebounds) {
        this.defensiveRebounds = defensiveRebounds;
    }

    public int getFouls() {
        return fouls;
    }

    public void setFouls(int fouls) {
        this.fouls = fouls;
    }

    @Override
    public String toString() {
        return "StatsDTO{" +
                "statsId=" + statsId +
                ", matchId=" + matchId +
                ", playerId=" + playerId +
                ", playerJersey='" + playerJersey + '\'' +
                ", teamName='" + teamName + '\'' +
                ", threePointers=" + threePointers +
                ", twoPointers=" + twoPointers +
                ", onePointers=" + onePointers +
                ", missedThreePointers=" + missedThreePointers +
                ", missedTwoPointers=" + missedTwoPointers +
                ", missedOnePointers=" + missedOnePointers +
                ", steals=" + steals +
                ", turnovers=" + turnovers +
                ", blocks=" + blocks +
                ", assists=" + assists +
                ", offensiveRebounds=" + offensiveRebounds +
                ", defensiveRebounds=" + defensiveRebounds +
                ", fouls=" + fouls +
                '}';
    }
}