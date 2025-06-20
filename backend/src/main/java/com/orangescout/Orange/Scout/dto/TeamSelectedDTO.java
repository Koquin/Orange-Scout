package com.orangescout.Orange.Scout.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.Valid;

import java.util.List;
import java.util.ArrayList;

public class TeamSelectedDTO {

    private Long id;

    @NotBlank(message = "O nome do time não pode estar em branco.")
    @Size(min = 2, max = 100, message = "O nome do time deve ter entre 2 e 100 caracteres.")
    private String teamName;

    @Size(max = 255, message = "O caminho do logo deve ter no máximo 255 caracteres.")
    private String logoPath;

    @NotBlank(message = "A abreviação do time não pode estar em branco.")
    @Size(min = 2, max = 10, message = "A abreviação do time deve ter entre 2 e 10 caracteres.")
    private String abbreviation;

    @NotNull(message = "A lista de jogadores não pode ser nula.")
    @Valid
    private List<PlayerDTO> players = new ArrayList<>();

    public TeamSelectedDTO() {
    }

    public TeamSelectedDTO(Long id, String teamName, String logoPath, String abbreviation, List<PlayerDTO> players) {
        this.id = id;
        this.teamName = teamName;
        this.logoPath = logoPath;
        this.abbreviation = abbreviation;
        this.players = players;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public List<PlayerDTO> getPlayers() { // PADRONIZAÇÃO: Retorna List<PlayerDTO>
        return players;
    }

    public void setPlayers(List<PlayerDTO> players) { // PADRONIZAÇÃO: Recebe List<PlayerDTO>
        this.players = players;
    }

    @Override
    public String toString() {
        return "TeamSelectedDTO{" +
                "id=" + id +
                ", teamName='" + teamName + '\'' +
                ", logoPath='" + logoPath + '\'' +
                ", abbreviation='" + abbreviation + '\'' +
                ", playersCount=" + (players != null ? players.size() : 0) +
                '}';
    }
}