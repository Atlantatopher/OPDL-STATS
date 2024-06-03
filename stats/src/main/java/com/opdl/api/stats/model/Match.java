package com.opdl.api.stats.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "Match")
@JsonIgnoreProperties("schedule")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "schedule_id")
    private long scheduleId;

    @Column(name = "season_id")
    private long seasonId;

    @OneToOne
    private Team homeTeam;

    @OneToOne
    private Team awayTeam;

    @Transient
    private int homeScore;

    @Transient
    private int awayScore;

    public Match() {
    }

    public Match(long id) {
        this.id = id;
    }

    public Match(long id, long scheduleId, long seasonId, Team homeTeam, Team awayTeam) {
        this.id = id;
        this.scheduleId = scheduleId;
        this.seasonId = seasonId;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public long getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(long seasonId) {
        this.seasonId = seasonId;
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(Team homeTeam) {
        this.homeTeam = homeTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(Team awayTeam) {
        this.awayTeam = awayTeam;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(int homeScore) {
        this.homeScore = homeScore;
    }

    public int getAwayScore() {
        return awayScore;
    }

    public void setAwayScore(int awayScore) {
        this.awayScore = awayScore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Match match = (Match) o;
        return id == match.id && scheduleId == match.scheduleId && seasonId == match.seasonId && homeScore == match.homeScore && awayScore == match.awayScore && Objects.equals(homeTeam, match.homeTeam) && Objects.equals(awayTeam, match.awayTeam);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, scheduleId, seasonId, homeTeam, awayTeam, homeScore, awayScore);
    }

    @Override
    public String toString() {
        return "Match{" +
                "id=" + id +
                ", scheduleId=" + scheduleId +
                ", seasonId=" + seasonId +
                ", homeTeam=" + homeTeam +
                ", awayTeam=" + awayTeam +
                ", homeScore=" + homeScore +
                ", awayScore=" + awayScore +
                '}';
    }
}
