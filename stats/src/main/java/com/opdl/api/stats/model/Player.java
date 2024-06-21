package com.opdl.api.stats.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Player")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "season_id")
    private long seasonId;

    @Column(name = "player_name")
    private String playerName;

    @Column(name = "email_address")
    private String emailAddress;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "player_type")
    private String playerType;

    @Column(name = "isPlayoffEligible")
    private boolean isPlayoffEligible = false;

    @OneToOne(cascade = CascadeType.MERGE)
    private Team team;

    @OneToMany(mappedBy = "player")
    private List<Quality> quality;

    public Player() {
    }

    public Player(long id) {
        this.id = id;
    }

    public Player(long id, long seasonId, String playerName, String emailAddress, String phoneNumber, String playerType, boolean isPlayoffEligible, Team team) {
        this.id = id;
        this.seasonId = seasonId;
        this.playerName = playerName;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.playerType = playerType;
        this.isPlayoffEligible = isPlayoffEligible;
        this.team = team;
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

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPlayerType() {
        return playerType;
    }

    public void setPlayerType(String playerType) {
        this.playerType = playerType;
    }

    public boolean isPlayoffEligible() {
        return isPlayoffEligible;
    }

    public void setPlayoffEligible(boolean playoffEligible) {
        isPlayoffEligible = playoffEligible;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return id == player.id && seasonId == player.seasonId && isPlayoffEligible == player.isPlayoffEligible && Objects.equals(playerName, player.playerName) && Objects.equals(emailAddress, player.emailAddress) && Objects.equals(phoneNumber, player.phoneNumber) && Objects.equals(playerType, player.playerType) && Objects.equals(team, player.team);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, seasonId, playerName, emailAddress, phoneNumber, playerType, isPlayoffEligible, team);
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", seasonId=" + seasonId +
                ", playerName='" + playerName + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", playerType='" + playerType + '\'' +
                ", isPlayoffEligible=" + isPlayoffEligible +
                ", team=" + team +
                '}';
    }
}
