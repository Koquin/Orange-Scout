package com.orangescout.Orange.Scout.repository;

import com.orangescout.Orange.Scout.model.StartersTeamTwo; // PADRONIZAÇÃO: Renomeado
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying; // Importar @Modifying
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional; // Importar @Transactional

import java.util.Optional;

@Repository
public interface StartersTeamTwoRepository extends JpaRepository<StartersTeamTwo, Long> {
    Optional<StartersTeamTwo> findByMatchId(Long matchId);

    @Modifying
    @Transactional
    void deleteByMatchId(Long matchId);
}