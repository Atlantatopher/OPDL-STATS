package com.opdl.api.stats.model;

import java.util.Objects;


public class GameStats implements Comparable<GameStats> {

    private GameType gameType;

    private int numWins;

    private int numLosses;

    private double winPercentage;

    public GameStats() {
    }

    public GameStats(GameType gameType) {
        this.gameType = gameType;
    }

    public GameStats(GameType gameType, int numWins, int numLosses, double winPercentage) {
        this.gameType = gameType;
        this.numWins = numWins;
        this.numLosses = numLosses;
        this.winPercentage = winPercentage;
    }

    public GameType getGameType() {
        return gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public int getNumWins() {
        return numWins;
    }

    public void setNumWins(int numWins) {
        this.numWins = numWins;
    }

    public int getNumLosses() {
        return numLosses;
    }

    public void setNumLosses(int numLosses) {
        this.numLosses = numLosses;
    }

    public double getWinPercentage() {
        return winPercentage;
    }

    public void setWinPercentage(double winPercentage) {
        this.winPercentage = winPercentage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameStats gameStats = (GameStats) o;
        return numWins == gameStats.numWins && numLosses == gameStats.numLosses && Double.compare(winPercentage, gameStats.winPercentage) == 0 && Objects.equals(gameType, gameStats.gameType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameType, numWins, numLosses, winPercentage);
    }

    @Override
    public String toString() {
        return "GameStats{" +
                "gameType=" + gameType +
                ", numWins=" + numWins +
                ", numLosses=" + numLosses +
                ", winPercentage=" + winPercentage +
                '}';
    }

    @Override
    public int compareTo(GameStats otherTeam) {
        int result = Double.compare(otherTeam.getWinPercentage(), getWinPercentage());
        if ( result == 0 ) {
            result = Integer.compare(otherTeam.getNumWins(), getNumWins());
        }

        return result;
    }
}
