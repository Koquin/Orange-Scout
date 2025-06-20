package com.orangescout.Orange.Scout.repository;

import com.orangescout.Orange.Scout.model.StartersTeamOne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface StartersTeamOneRepository extends JpaRepository<StartersTeamOne, Long> {
    Optional<StartersTeamOne> findByMatchId(Long matchId);

    @Modifying
    @Transactional
    void deleteByMatchId(Long matchId);
}