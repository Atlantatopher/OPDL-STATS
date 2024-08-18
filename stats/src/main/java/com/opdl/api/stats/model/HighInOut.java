package com.opdl.api.stats.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "HighInOut")
public class HighInOut {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "season_id")
    private long seasonId;

    @OneToOne
    private Match match;

    @ManyToOne
    private Player player;

    @Column(name = "high_in")
    private int highIn = 0;

    @Column(name = "high_out")
    private int highOut = 0;

    public HighInOut() {
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

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getHighIn() {
        return highIn;
    }

    public void setHighIn(int highIn) {
        this.highIn = highIn;
    }

    public int getHighOut() {
        return highOut;
    }

    public void setHighOut(int highOut) {
        this.highOut = highOut;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HighInOut highInOut = (HighInOut) o;
        return id == highInOut.id && seasonId == highInOut.seasonId && highIn == highInOut.highIn && highOut == highInOut.highOut && Objects.equals(match, highInOut.match) && Objects.equals(player, highInOut.player);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, seasonId, match, player, highIn, highOut);
    }

    @Override
    public String toString() {
        return "HighInOut{" +
                "id=" + id +
                ", seasonId=" + seasonId +
                ", match=" + match +
                ", player=" + player +
                ", highIn=" + highIn +
                ", highOut=" + highOut +
                '}';
    }
}
