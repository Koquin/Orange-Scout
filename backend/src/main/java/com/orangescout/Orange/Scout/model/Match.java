package com.orangescout.Orange.Scout.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "matches")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_one_id", nullable = false)
    @JsonBackReference("team-homeMatches")
    private Team teamOne;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_two_id", nullable = false)
    @JsonBackReference("team-awayMatches")
    private Team teamTwo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference("match-stats")
    private List<Stats> stats = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    @JsonManagedReference("match-location")
    private Location location;

    @Column(name = "team_one_score", nullable = false)
    private int teamOneScore;

    @Column(name = "team_two_score", nullable = false)
    private int teamTwoScore;

    @Column(name = "match_date", nullable = false)
    private LocalDate matchDate;

    @Column(nullable = false)
    private boolean finished = false;

    @Column(name = "game_mode", length = 50, nullable = false)
    private String gameMode;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "starters_team_one_id")
    @JsonManagedReference("match-starters-teamOne")
    private StartersTeamOne startersTeamOne;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "starters_team_two_id")
    @JsonManagedReference("match-starters-teamTwo")
    private StartersTeamTwo startersTeamTwo;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Match() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Team getTeamOne() {
        return teamOne;
    }

    public void setTeamOne(Team teamOne) {
        this.teamOne = teamOne;
    }

    public Team getTeamTwo() {
        return teamTwo;
    }

    public void setTeamTwo(Team teamTwo) {
        this.teamTwo = teamTwo;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }
    public List<Stats> getStats() {
        return stats;
    }

    public void setStats(List<Stats> stats) {
        this.stats = stats;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
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

    public LocalDate getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(LocalDate matchDate) {
        this.matchDate = matchDate;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public String getGameMode() {
        return gameMode;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    public StartersTeamOne getStartersTeamOne() {
        return startersTeamOne;
    }

    public void setStartersTeamOne(StartersTeamOne startersTeamOne) {
        this.startersTeamOne = startersTeamOne;
    }

    public StartersTeamTwo getStartersTeamTwo() {
        return startersTeamTwo;
    }

    public void setStartersTeamTwo(StartersTeamTwo startersTeamTwo) {
        this.startersTeamTwo = startersTeamTwo;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Match match = (Match) o;
        return Objects.equals(id, match.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Match{" +
                "id=" + id +
                ", teamOne=" + (teamOne != null ? teamOne.getName() : "null") +
                ", teamTwo=" + (teamTwo != null ? teamTwo.getName() : "null") +
                ", AppUser=" + (user != null ? user.getAppUsername() : "null") +
                ", teamOneScore=" + teamOneScore +
                ", teamTwoScore=" + teamTwoScore +
                ", matchDate=" + matchDate +
                ", finished=" + finished +
                ", gameMode='" + gameMode + '\'' +
                '}';
    }
}