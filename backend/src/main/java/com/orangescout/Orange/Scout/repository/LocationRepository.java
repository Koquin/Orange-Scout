package com.orangescout.Orange.Scout.repository;

import com.orangescout.Orange.Scout.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
}