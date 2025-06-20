package com.orangescout.Orange.Scout.repository;

import com.orangescout.Orange.Scout.model.Stats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface StatsRepository extends JpaRepository<Stats, Long> {
    List<Stats> findByMatchId(Long matchId);

    @Modifying
    @Transactional
    void deleteByMatchId(Long matchId);

    Optional<Stats> findByMatchIdAndPlayerId(Long matchId, Long playerId);
}