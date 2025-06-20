package com.orangescout.Orange.Scout.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "starters_team_two")
public class StartersTeamTwo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "startersTeamTwo", fetch = FetchType.LAZY)
    @JsonBackReference("match-starters-teamTwo")
    private Match match;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "starters_team_two_players",
            joinColumns = @JoinColumn(name = "starters_team_two_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    @JsonManagedReference("starters-players")
    private List<Player> starters = new ArrayList<>();

    public StartersTeamTwo() {}

    public StartersTeamTwo(Match match, List<Player> starters) {
        this.match = match;
        this.starters = starters;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public List<Player> getStarters() {
        return starters;
    }

    public void setStarters(List<Player> starters) {
        this.starters = starters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StartersTeamTwo that = (StartersTeamTwo) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "StartersTeamTwo{" +
                "id=" + id +
                ", matchId=" + (match != null ? match.getId() : "null") +
                ", startersCount=" + (starters != null ? starters.size() : 0) +
                '}';
    }
}