package com.orangescout.Orange.Scout.dto;

public class PlayerDTO {
    private Long id_player;
    private String playerName;
    private String jerseyNumber;

    public PlayerDTO(Long id_player, String playerName, String jerseyNumber) {
        this.id_player = id_player;
        this.playerName = playerName;
        this.jerseyNumber = jerseyNumber;
    }

    public PlayerDTO(){};

    public Long getId_player() {
        return id_player;
    }

    public void setId_player(Long id_player) {
        this.id_player = id_player;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getJerseyNumber() {
        return jerseyNumber;
    }

    public void setJerseyNumber(String jerseyNumber) {
        this.jerseyNumber = jerseyNumber;
    }

    @Override
    public String toString() {
        return "PlayerDTO{" +
                "id_player=" + id_player +
                ", playerName='" + playerName + '\'' +
                ", jerseyNumber=" + jerseyNumber +
                '}';
    }
}
