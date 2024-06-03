package com.opdl.api.stats.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "Game")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne(cascade = CascadeType.MERGE)
    private Match match;

    @OneToOne(cascade = CascadeType.MERGE)
    private GameType gameType;

    @Column(name = "is_doubles")
    private boolean isDoubles;

    @Column(name = "home_score")
    private int homeScore;

    @Column(name = "away_score")
    private int awayScore;

    @OneToOne
    @JoinColumn(name="home_player_id", referencedColumnName="id")
    private Player homePlayer;

    @OneToOne
    @JoinColumn(name="home_player_id_dbl", referencedColumnName="id")
    private Player homePlayerDBL;

    @OneToOne
    @JoinColumn(name="away_player_id", referencedColumnName="id")
    private Player awayPlayer;

    @OneToOne
    @JoinColumn(name="away_player_id_dbl", referencedColumnName="id")
    private Player awayPlayerDBL;

    public Game() {
    }

    public Game(long id, Match match, GameType gameType, boolean isDoubles, int homeScore, int awayScore, Player homePlayer, Player homePlayerDBL, Player awayPlayer, Player awayPlayerDBL) {
        this.id = id;
        this.match = match;
        this.gameType = gameType;
        this.isDoubles = isDoubles;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.homePlayer = homePlayer;
        this.homePlayerDBL = homePlayerDBL;
        this.awayPlayer = awayPlayer;
        this.awayPlayerDBL = awayPlayerDBL;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public GameType getGameType() {
        return gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public boolean isDoubles() {
        return isDoubles;
    }

    public void setDoubles(boolean doubles) {
        isDoubles = doubles;
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

    public Player getHomePlayer() {
        return homePlayer;
    }

    public void setHomePlayer(Player homePlayer) {
        this.homePlayer = homePlayer;
    }

    public Player getHomePlayerDBL() {
        return homePlayerDBL;
    }

    public void setHomePlayerDBL(Player homePlayerDBL) {
        this.homePlayerDBL = homePlayerDBL;
    }

    public Player getAwayPlayer() {
        return awayPlayer;
    }

    public void setAwayPlayer(Player awayPlayer) {
        this.awayPlayer = awayPlayer;
    }

    public Player getAwayPlayerDBL() {
        return awayPlayerDBL;
    }

    public void setAwayPlayerDBL(Player awayPlayerDBL) {
        this.awayPlayerDBL = awayPlayerDBL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return id == game.id && match == game.match && isDoubles == game.isDoubles && homeScore == game.homeScore && awayScore == game.awayScore && Objects.equals(gameType, game.gameType) && Objects.equals(homePlayer, game.homePlayer) && Objects.equals(homePlayerDBL, game.homePlayerDBL) && Objects.equals(awayPlayer, game.awayPlayer) && Objects.equals(awayPlayerDBL, game.awayPlayerDBL);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, match, gameType, isDoubles, homeScore, awayScore, homePlayer, homePlayerDBL, awayPlayer, awayPlayerDBL);
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", match=" + match +
                ", gameType=" + gameType +
                ", isDoubles=" + isDoubles +
                ", homeScore=" + homeScore +
                ", awayScore=" + awayScore +
                ", homePlayer=" + homePlayer +
                ", homePlayerDBL=" + homePlayerDBL +
                ", awayPlayer=" + awayPlayer +
                ", awayPlayerDBL=" + awayPlayerDBL +
                '}';
    }
}
