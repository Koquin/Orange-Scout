package com.orangescout.Orange.Scout.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

    @NotBlank(message = "O email não pode estar em branco.")
    @Email(message = "Formato de email inválido.")
    @Size(max = 100, message = "O email deve ter no máximo 100 caracteres.")
    private String email;

    @NotBlank(message = "A senha não pode estar em branco.")
    @Size(min = 6, max = 50, message = "A senha deve ter entre 6 e 50 caracteres.")
    private String password;

    @NotBlank(message = "O nome de usuário não pode estar em branco.")
    @Size(min = 3, max = 50, message = "O nome de usuário deve ter entre 3 e 50 caracteres.")
    private String username;

    public RegisterRequest() {
    }

    public RegisterRequest(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() { // PADRONIZAÇÃO: Getter para username
        return username;
    }

    public void setUsername(String username) { // PADRONIZAÇÃO: Setter para username
        this.username = username;
    }

    @Override
    public String toString() {
        return "RegisterRequest{" +
                "email='" + email + '\'' +
                ", password='[PROTECTED]'" +
                ", username='" + username + '\'' +
                '}';
    }
}