package com.orangescout.Orange.Scout.repository;

import com.orangescout.Orange.Scout.model.AppUser;
import com.orangescout.Orange.Scout.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
    List<Match> findByUser(AppUser user);
    List<Match> findByUserAndFinishedTrue(AppUser user);
    Optional<Match> findTopByUserAndFinishedFalseOrderByIdDesc(AppUser user);
}