package com.opdl.api.stats.repository;

import com.opdl.api.stats.model.Bar;
import com.opdl.api.stats.model.GameType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameTypeRepository extends JpaRepository<GameType, Long> {
}
