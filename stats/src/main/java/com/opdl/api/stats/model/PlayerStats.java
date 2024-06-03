package com.opdl.api.stats.model;

import java.util.Objects;


public class PlayerStats implements Comparable<PlayerStats> {

    private Player player;

    private Team team;

    private GameStats singlesCricket = new GameStats(GameType.CRICKET);
    private GameStats singlesX01 = new GameStats(GameType.X01);
    private GameStats doublesCricket = new GameStats(GameType.CRICKET);
    private GameStats doublesX01 = new GameStats(GameType.X01);
    private GameStats overall = new GameStats(new GameType());

    public PlayerStats() {
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public GameStats getSinglesCricket() {
        return singlesCricket;
    }

    public void setSinglesCricket(GameStats singlesCricket) {
        this.singlesCricket = singlesCricket;
    }

    public GameStats getSinglesX01() {
        return singlesX01;
    }

    public void setSinglesX01(GameStats singlesX01) {
        this.singlesX01 = singlesX01;
    }

    public GameStats getDoublesCricket() {
        return doublesCricket;
    }

    public void setDoublesCricket(GameStats doublesCricket) {
        this.doublesCricket = doublesCricket;
    }

    public GameStats getDoublesX01() {
        return doublesX01;
    }

    public void setDoublesX01(GameStats doublesX01) {
        this.doublesX01 = doublesX01;
    }

    public GameStats getOverall() {
        return overall;
    }

    public void setOverall(GameStats overall) {
        this.overall = overall;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerStats that = (PlayerStats) o;
        return Objects.equals(player, that.player) && Objects.equals(team, that.team) && Objects.equals(singlesCricket, that.singlesCricket) && Objects.equals(singlesX01, that.singlesX01) && Objects.equals(doublesCricket, that.doublesCricket) && Objects.equals(doublesX01, that.doublesX01) && Objects.equals(overall, that.overall);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player, team, singlesCricket, singlesX01, doublesCricket, doublesX01, overall);
    }

    @Override
    public String toString() {
        return "PlayerStats{" +
                "player=" + player +
                ", team=" + team +
                ", singlesCricket=" + singlesCricket +
                ", singlesX01=" + singlesX01 +
                ", doublesCricket=" + doublesCricket +
                ", doublesX01=" + doublesX01 +
                ", overall=" + overall +
                '}';
    }

    @Override
    public int compareTo(PlayerStats otherTeam) {
        return overall.compareTo(otherTeam.overall);

    }
}
