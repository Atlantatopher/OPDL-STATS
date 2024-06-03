package com.opdl.api.stats.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "Team")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "season_id")
    private long seasonId;

    @Column(name = "team_name")
    private String teamName;

    @OneToOne
    private Bar homeBar;

    public Team() {
    }

    public Team(long id) {
        this.id = id;
    }

    public Team(long id, long seasonId, String teamName, Bar homeBar) {
        this.id = id;
        this.seasonId = seasonId;
        this.teamName = teamName;
        this.homeBar = homeBar;
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

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Bar getHomeBar() {
        return homeBar;
    }

    public void setHomeBar(Bar homeBar) {
        this.homeBar = homeBar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return id == team.id && seasonId == team.seasonId && Objects.equals(teamName, team.teamName) && Objects.equals(homeBar, team.homeBar);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, seasonId, teamName, homeBar);
    }

    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", seasonId=" + seasonId +
                ", teamName='" + teamName + '\'' +
                ", homeBar=" + homeBar +
                '}';
    }
}
