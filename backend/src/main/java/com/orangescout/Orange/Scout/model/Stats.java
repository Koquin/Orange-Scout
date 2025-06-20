package com.orangescout.Orange.Scout.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "stats")
public class Stats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id")
    @JsonBackReference("match-stats")
    private Match match;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @Column(name = "three_pointers", nullable = false)
    private int threePointers = 0;

    @Column(name = "two_pointers", nullable = false)
    private int twoPointers = 0;

    @Column(name = "one_pointers", nullable = false)
    private int onePointers = 0;

    @Column(name = "missed_three_pointers", nullable = false)
    private int missedThreePointers = 0;

    @Column(name = "missed_two_pointers", nullable = false)
    private int missedTwoPointers = 0;

    @Column(name = "missed_one_pointers", nullable = false)
    private int missedOnePointers = 0;

    @Column(name = "steals", nullable = false)
    private int steals = 0;

    @Column(name = "turnovers", nullable = false)
    private int turnovers = 0;

    @Column(name = "blocks", nullable = false)
    private int blocks = 0;

    @Column(name = "assists", nullable = false)
    private int assists = 0;

    @Column(name = "offensive_rebounds", nullable = false)
    private int offensiveRebounds = 0;

    @Column(name = "defensive_rebounds", nullable = false)
    private int defensiveRebounds = 0;

    @Column(name = "fouls", nullable = false)
    private int fouls = 0;

    public Stats() {
    }

    public Stats(Player player, Match match, Team team) {
        this.player = player;
        this.match = match;
        this.team = team;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stats stats = (Stats) o;
        return Objects.equals(id, stats.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Stats{" +
                "id=" + id +
                ", playerId=" + (player != null ? player.getId() : "null") +
                ", matchId=" + (match != null ? match.getId() : "null") +
                ", teamId=" + (team != null ? team.getId() : "null") +
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