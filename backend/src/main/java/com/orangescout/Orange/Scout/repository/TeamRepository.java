package com.orangescout.Orange.Scout.repository;

import com.orangescout.Orange.Scout.model.AppUser;
import com.orangescout.Orange.Scout.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    List<Team> findByUser(AppUser user);

    List<Team> findByUserId(Long userId);
}