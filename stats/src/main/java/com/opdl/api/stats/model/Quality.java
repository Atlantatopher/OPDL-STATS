package com.opdl.api.stats.model;

import jakarta.persistence.*;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.util.Objects;

@Entity
@Table(name = "Quality")
public class Quality {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "season_id")
    private long seasonId;

    @OneToOne
    private Match match;

    @ManyToOne
    private Player player;

    @Column(name = "ton_80")
    private int numTon80 = 0;

    @Column(name = "ton_40")
    private int numTon40 = 0;

    @Column(name = "ton_20")
    private int numTon20 = 0;

    @Column(name = "ton")
    private int numTon = 0;

    @Column(name = "mark_9")
    private int numMark9 = 0;

    @Column(name = "mark_8")
    private int numMark8 = 0;

    @Column(name = "mark_7")
    private int numMark7 = 0;

    @Column(name = "mark_6")
    private int numMark6 = 0;

    @Column(name = "cork_6")
    private int numCork6 = 0;

    @Column(name = "cork_5")
    private int numCork5 = 0;

    @Column(name = "cork_4")
    private int numCork4 = 0;
    
    @Column(name = "cork_3")
    private int numCork3 = 0;

    @Transient
    private int total = 0;

    public Quality() {
    }

    public Quality(long id, long seasonId, Match match, Player player, int numTon80, int numTon40, int numTon20, int numTon, int numMark9, int numMark8, int numMark7, int numMark6, int numCork6, int numCork5, int numCork4, int numCork3) {
        this.id = id;
        this.seasonId = seasonId;
        this.match = match;
        this.player = player;
        this.numTon80 = numTon80;
        this.numTon40 = numTon40;
        this.numTon20 = numTon20;
        this.numTon = numTon;
        this.numMark9 = numMark9;
        this.numMark8 = numMark8;
        this.numMark7 = numMark7;
        this.numMark6 = numMark6;
        this.numCork6 = numCork6;
        this.numCork5 = numCork5;
        this.numCork4 = numCork4;
        this.numCork3 = numCork3;
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

    public int getNumTon80() {
        return numTon80;
    }

    public void setNumTon80(int numTon80) {
        this.numTon80 = numTon80;
    }

    public int getNumTon40() {
        return numTon40;
    }

    public void setNumTon40(int numTon40) {
        this.numTon40 = numTon40;
    }

    public int getNumTon20() {
        return numTon20;
    }

    public void setNumTon20(int numTon20) {
        this.numTon20 = numTon20;
    }

    public int getNumTon() {
        return numTon;
    }

    public void setNumTon(int numTon) {
        this.numTon = numTon;
    }

    public int getNumMark9() {
        return numMark9;
    }

    public void setNumMark9(int numMark9) {
        this.numMark9 = numMark9;
    }

    public int getNumMark8() {
        return numMark8;
    }

    public void setNumMark8(int numMark8) {
        this.numMark8 = numMark8;
    }

    public int getNumMark7() {
        return numMark7;
    }

    public void setNumMark7(int numMark7) {
        this.numMark7 = numMark7;
    }

    public int getNumMark6() {
        return numMark6;
    }

    public void setNumMark6(int numMark6) {
        this.numMark6 = numMark6;
    }

    public int getNumCork6() {
        return numCork6;
    }

    public void setNumCork6(int numCork6) {
        this.numCork6 = numCork6;
    }

    public int getNumCork5() {
        return numCork5;
    }

    public void setNumCork5(int numCork5) {
        this.numCork5 = numCork5;
    }

    public int getNumCork4() {
        return numCork4;
    }

    public void setNumCork4(int numCork4) {
        this.numCork4 = numCork4;
    }

    public int getNumCork3() {
        return numCork3;
    }

    public void setNumCork3(int numCork3) {
        this.numCork3 = numCork3;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quality quality = (Quality) o;
        return id == quality.id && seasonId == quality.seasonId && match == quality.match && numTon80 == quality.numTon80 && numTon40 == quality.numTon40 && numTon20 == quality.numTon20 && numTon == quality.numTon && numMark9 == quality.numMark9 && numMark8 == quality.numMark8 && numMark7 == quality.numMark7 && numMark6 == quality.numMark6 && numCork6 == quality.numCork6 && numCork5 == quality.numCork5 && numCork4 == quality.numCork4 && numCork3 == quality.numCork3 && Objects.equals(player, quality.player);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, seasonId, match, player, numTon80, numTon40, numTon20, numTon, numMark9, numMark8, numMark7, numMark6, numCork6, numCork5, numCork4, numCork3);
    }

    @Override
    public String toString() {
        return "Quality{" +
                "id=" + id +
                ", seasonId=" + seasonId +
                ", match=" + match +
                ", player=" + player +
                ", numTon80=" + numTon80 +
                ", numTon40=" + numTon40 +
                ", numTon20=" + numTon20 +
                ", numTon=" + numTon +
                ", numMark9=" + numMark9 +
                ", numMark8=" + numMark8 +
                ", numMark7=" + numMark7 +
                ", numMark6=" + numMark6 +
                ", numCork6=" + numCork6 +
                ", numCork5=" + numCork5 +
                ", numCork4=" + numCork4 +
                ", numCork3=" + numCork3 +
                '}';
    }
}
