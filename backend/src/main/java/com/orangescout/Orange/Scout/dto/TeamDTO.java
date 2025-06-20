package com.orangescout.Orange.Scout.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TeamDTO {

    private Long id;

    @NotBlank(message = "O nome do time não pode estar em branco.")
    @Size(min = 2, max = 100, message = "O nome do time deve ter entre 2 e 100 caracteres.")
    private String teamName;

    @Size(max = 255, message = "O caminho do logo deve ter no máximo 255 caracteres.")
    private String logoPath;

    @NotBlank(message = "A abreviação do time não pode estar em branco.")
    @Size(min = 2, max = 10, message = "A abreviação do time deve ter entre 2 e 10 caracteres.")
    private String abbreviation;

    private Long userId;

    public TeamDTO() {
    }

    public TeamDTO(Long id, String teamName, String logoPath, String abbreviation, Long userId) {
        this.id = id;
        this.teamName = teamName;
        this.logoPath = logoPath;
        this.abbreviation = abbreviation;
        this.userId = userId;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "TeamDTO{" +
                "id=" + id +
                ", teamName='" + teamName + '\'' +
                ", logoPath='" + logoPath + '\'' +
                ", abbreviation='" + abbreviation + '\'' +
                ", userId=" + userId +
                '}';
    }
}