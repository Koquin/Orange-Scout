package com.orangescout.Orange.Scout.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginResponse {

    @NotBlank(message = "O token não pode estar em branco.")
    private String token;

    public LoginResponse() {
    }

    public LoginResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "token='[REDACTED]'" +
                '}';
    }
}