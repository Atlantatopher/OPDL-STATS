package com.opdl.api.stats.model;

import jakarta.persistence.*;

import java.util.Objects;

public class HighInOutReport {

    private Player player;

    private int numHighIns = 0;

    private int numHighOuts = 0;

    private int highestIn = 0;

    private int highestOut = 0;

    public HighInOutReport() {
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getNumHighIns() {
        return numHighIns;
    }

    public void setNumHighIns(int numHighIns) {
        this.numHighIns = numHighIns;
    }

    public int getNumHighOuts() {
        return numHighOuts;
    }

    public void setNumHighOuts(int numHighOuts) {
        this.numHighOuts = numHighOuts;
    }

    public int getHighestIn() {
        return highestIn;
    }

    public void setHighestIn(int highestIn) {
        this.highestIn = highestIn;
    }

    public int getHighestOut() {
        return highestOut;
    }

    public void setHighestOut(int highestOut) {
        this.highestOut = highestOut;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HighInOutReport that = (HighInOutReport) o;
        return numHighIns == that.numHighIns && numHighOuts == that.numHighOuts && highestIn == that.highestIn && highestOut == that.highestOut && Objects.equals(player, that.player);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player, numHighIns, numHighOuts, highestIn, highestOut);
    }

    @Override
    public String toString() {
        return "HighInOutReport{" +
                "player=" + player +
                ", numHighIns=" + numHighIns +
                ", numHighOuts=" + numHighOuts +
                ", highestIn=" + highestIn +
                ", highestOut=" + highestOut +
                '}';
    }
}
