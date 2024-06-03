package com.opdl.api.stats.repository;

import com.opdl.api.stats.model.Season;
import com.opdl.api.stats.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeasonRepository extends JpaRepository<Season, Long> {
}
