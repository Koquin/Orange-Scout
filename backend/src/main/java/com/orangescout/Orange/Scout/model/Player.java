package com.orangescout.Orange.Scout.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "players")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    @JsonBackReference("team-players")
    private Team team;

    @Column(name = "player_name", nullable = false, length = 100)
    private String playerName;

    @Column(name = "jersey_number", nullable = false, length = 10)
    private String jerseyNumber;

    public Player() {
    }

    public Player(Team team, String playerName, String jerseyNumber) {
        this.team = team;
        this.playerName = playerName;
        this.jerseyNumber = jerseyNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getJerseyNumber() {
        return jerseyNumber;
    }

    public void setJerseyNumber(String jerseyNumber) {
        this.jerseyNumber = jerseyNumber;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(id, player.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", team=" + (team != null ? team.getName() : "null") +
                ", playerName='" + playerName + '\'' +
                ", jerseyNumber='" + jerseyNumber + '\'' +
                '}';
    }
}