package com.opdl.api.stats.model;

import java.util.Objects;

public class TeamStandings {

    private Team team;

    private int score;

    private int totalGamesPlayed;

    private double winPercentage;

    private int numGamesBehind;

    private int scoreAdjusted;

    public TeamStandings() {
    }

    public TeamStandings(Team team, int score) {
        this.team = team;
        this.score = score;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTotalGamesPlayed() {
        return totalGamesPlayed;
    }

    public void setTotalGamesPlayed(int totalGamesPlayed) {
        this.totalGamesPlayed = totalGamesPlayed;
    }

    public double getWinPercentage() {
        return winPercentage;
    }

    public void setWinPercentage(double winPercentage) {
        this.winPercentage = winPercentage;
    }

    public int getNumGamesBehind() {
        return numGamesBehind;
    }

    public void setNumGamesBehind(int numGamesBehind) {
        this.numGamesBehind = numGamesBehind;
    }

    public int getScoreAdjusted() {
        return scoreAdjusted;
    }

    public void setScoreAdjusted(int scoreAdjusted) {
        this.scoreAdjusted = scoreAdjusted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeamStandings that = (TeamStandings) o;
        return score == that.score && totalGamesPlayed == that.totalGamesPlayed && Double.compare(winPercentage, that.winPercentage) == 0 && numGamesBehind == that.numGamesBehind && scoreAdjusted == that.scoreAdjusted && Objects.equals(team, that.team);
    }

    @Override
    public int hashCode() {
        return Objects.hash(team, score, totalGamesPlayed, winPercentage, numGamesBehind, scoreAdjusted);
    }

    @Override
    public String toString() {
        return "TeamStandings{" +
                "team=" + team +
                ", score=" + score +
                ", totalGamesPlayed=" + totalGamesPlayed +
                ", winPercentage=" + winPercentage +
                ", numGamesBehind=" + numGamesBehind +
                ", scoreAdjusted=" + scoreAdjusted +
                '}';
    }
}
