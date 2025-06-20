package com.orangescout.Orange.Scout.repository;

import com.orangescout.Orange.Scout.model.Player;
import com.orangescout.Orange.Scout.model.Team; // Importar Team
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    List<Player> findAllByTeamId(Long teamId);

    List<Player> findByTeam(Team team);
}