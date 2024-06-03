package com.opdl.api.stats.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "Game_Type")
public class GameType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "description")
    private String description;

    public static GameType CRICKET = new GameType(1);
    public static GameType X01 = new GameType(2);

    public GameType() {
    }

    public GameType(long id) {
        this.id = id;
    }

    public GameType(long id, String description) {
        this.id = id;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameType gameType = (GameType) o;
        return id == gameType.id && Objects.equals(description, gameType.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description);
    }

    @Override
    public String toString() {
        return "GameType{" +
                "id=" + id +
                ", description='" + description + '\'' +
                '}';
    }
}
