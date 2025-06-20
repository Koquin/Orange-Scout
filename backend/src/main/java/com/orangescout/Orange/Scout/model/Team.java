package com.orangescout.Orange.Scout.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "teams")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "team_name", nullable = false, length = 100)
    private String name;

    @Column(name = "team_logo_path", length = 255)
    private String logoPath;

    @Column(name = "team_abbr", nullable = false, length = 10)
    private String abbr;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference("team-players")
    private List<Player> players = new ArrayList<>();

    @OneToMany(mappedBy = "teamOne", fetch = FetchType.LAZY)
    @JsonManagedReference("team-homeMatches")
    private List<Match> homeMatches = new ArrayList<>();

    @OneToMany(mappedBy = "teamTwo", fetch = FetchType.LAZY)
    @JsonManagedReference("team-awayMatches")
    private List<Match> awayMatches = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Team() {
    }

    public Team(String name, String abbr, AppUser AppUser) {
        this.name = name;
        this.abbr = abbr;
        this.user = AppUser;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() { // PADRONIZAÇÃO: Getter renomeado.
        return name;
    }

    public void setName(String name) { // PADRONIZAÇÃO: Setter renomeado.
        this.name = name;
    }

    public String getLogoPath() { // PADRONIZAÇÃO: Getter renomeado.
        return logoPath;
    }

    public void setLogoPath(String logoPath) { // PADRONIZAÇÃO: Setter renomeado.
        this.logoPath = logoPath;
    }

    public String getAbbr() { // PADRONIZAÇÃO: Getter renomeado.
        return abbr;
    }

    public void setAbbr(String abbr) { // PADRONIZAÇÃO: Setter renomeado.
        this.abbr = abbr;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public List<Match> getHomeMatches() { // PADRONIZAÇÃO: Getter renomeado.
        return homeMatches;
    }

    public void setHomeMatches(List<Match> homeMatches) { // PADRONIZAÇÃO: Setter renomeado.
        this.homeMatches = homeMatches;
    }

    public List<Match> getAwayMatches() {
        return awayMatches;
    }

    public void setAwayMatches(List<Match> awayMatches) {
        this.awayMatches = awayMatches;
    }

    public AppUser getAppUser() {
        return user;
    }

    public void setAppUser(AppUser AppUser) {
        this.user = AppUser;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return Objects.equals(id, team.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", abbr='" + abbr + '\'' +
                ", AppUser=" + (user != null ? user.getAppUsername() : "null") +
                ", playersCount=" + (players != null ? players.size() : 0) +
                ", homeMatchesCount=" + (homeMatches != null ? homeMatches.size() : 0) +
                ", awayMatchesCount=" + (awayMatches != null ? awayMatches.size() : 0) +
                '}';
    }
}