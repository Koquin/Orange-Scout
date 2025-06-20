package com.orangescout.Orange.Scout.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class LocationDTO {

    private Long id;

    @NotNull(message = "O nome do local não pode ser nulo.")
    @Size(min = 3, max = 255, message = "O nome do local deve ter entre 3 e 255 caracteres.")
    private String venueName;

    @NotNull(message = "A latitude não pode ser nula.")
    private Double latitude;

    @NotNull(message = "A longitude não pode ser nula.")
    private Double longitude;

    public LocationDTO() {
    }

    public LocationDTO(Long id, String venueName, Double latitude, Double longitude) {
        this.id = id;
        this.venueName = venueName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "LocationDTO{" +
                "id=" + id +
                ", venueName='" + venueName + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}