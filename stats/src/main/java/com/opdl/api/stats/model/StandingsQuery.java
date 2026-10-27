package com.opdl.api.stats.model;

import java.time.LocalDate;
import java.util.Objects;


public class StandingsQuery {

    private long seasonId;

    private int weekId;

    private LocalDate matchDate;

    private Team team;

    private Player player;

    public StandingsQuery() {
    }

    public StandingsQuery(long seasonId) {
        this.seasonId = seasonId;
    }

    public StandingsQuery(long seasonId, int weekId, LocalDate matchDate, Team team, Player player) {
        this.seasonId = seasonId;
        this.weekId = weekId;
        this.matchDate = matchDate;
        this.team = team;
        this.player = player;
    }

    public long getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(long seasonId) {
        this.seasonId = seasonId;
    }

    public int getWeekId() {
        return weekId;
    }

    public void setWeekId(int weekId) {
        this.weekId = weekId;
    }

    public LocalDate getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(LocalDate matchDate) {
        this.matchDate = matchDate;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StandingsQuery that = (StandingsQuery) o;
        return seasonId == that.seasonId && weekId == that.weekId && Objects.equals(matchDate, that.matchDate) && Objects.equals(team, that.team) && Objects.equals(player, that.player);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seasonId, weekId, matchDate, team, player);
    }

    @Override
    public String toString() {
        return "StandingsQuery{" +
                "seasonId=" + seasonId +
                ", weekId=" + weekId +
                ", matchDate=" + matchDate +
                ", team=" + team +
                ", player=" + player +
                '}';
    }
}
