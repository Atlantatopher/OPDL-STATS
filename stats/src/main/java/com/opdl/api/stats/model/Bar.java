package com.opdl.api.stats.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "Bar")
public class Bar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "season_id")
    private long seasonId;

    @Column(name = "bar_name")
    private String barName;

    @Column(name = "num_boards")
    private int numBoards;

    @Column(name = "num_teams")
    private int numTeams;

    @Column(name = "owner_name")
    private String ownerName;

    @Column(name = "email_address")
    private String emailAddress;

    @Column(name = "phone_number")
    private String phoneNumber;

    @OneToOne(cascade = CascadeType.ALL)
    private Address address;

    public Bar() {
    }

    public Bar(long id, long seasonId, String barName, int numBoards, int numTeams, String ownerName, String emailAddress, String phoneNumber, Address address) {
        this.id = id;
        this.seasonId = seasonId;
        this.barName = barName;
        this.numBoards = numBoards;
        this.numTeams = numTeams;
        this.ownerName = ownerName;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.address = address;
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

    public String getBarName() {
        return barName;
    }

    public void setBarName(String barName) {
        this.barName = barName;
    }

    public int getNumBoards() {
        return numBoards;
    }

    public void setNumBoards(int numBoards) {
        this.numBoards = numBoards;
    }

    public int getNumTeams() {
        return numTeams;
    }

    public void setNumTeams(int numTeams) {
        this.numTeams = numTeams;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bar bar = (Bar) o;
        return id == bar.id && seasonId == bar.seasonId && numBoards == bar.numBoards && numTeams == bar.numTeams && Objects.equals(barName, bar.barName) && Objects.equals(ownerName, bar.ownerName) && Objects.equals(emailAddress, bar.emailAddress) && Objects.equals(phoneNumber, bar.phoneNumber) && Objects.equals(address, bar.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, seasonId, barName, numBoards, numTeams, ownerName, emailAddress, phoneNumber, address);
    }

    @Override
    public String toString() {
        return "Bar{" +
                "id=" + id +
                ", seasonId=" + seasonId +
                ", barName='" + barName + '\'' +
                ", numBoards=" + numBoards +
                ", numTeams=" + numTeams +
                ", ownerName='" + ownerName + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", phone_number='" + phoneNumber + '\'' +
                ", address=" + address +
                '}';
    }
}
