package com.opdl.api.stats.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.springframework.format.datetime.DateFormatter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Schedule")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "season_id")
    private long seasonId;

    @Column(name = "week_id")
    private long weekId;

    @Column(name = "match_date")
    private LocalDate matchDate;

    public Schedule() {
    }

    public Schedule(long id, long seasonId, long weekId, LocalDate matchDate) {
        this.id = id;
        this.seasonId = seasonId;
        this.weekId = weekId;
        this.matchDate = matchDate;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(long seasonId) {
        this.seasonId = seasonId;
    }

    public long getWeekId() {
        return weekId;
    }

    public void setWeekId(long weekId) {
        this.weekId = weekId;
    }

    public LocalDate getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(LocalDate matchDate) {
        this.matchDate = matchDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Schedule schedule = (Schedule) o;
        return id == schedule.id && seasonId == schedule.seasonId && weekId == schedule.weekId && Objects.equals(matchDate, schedule.matchDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, seasonId, weekId, matchDate);
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "id=" + id +
                ", seasonId=" + seasonId +
                ", weekId=" + weekId +
                ", matchDate=" + matchDate +
                '}';
    }
}
