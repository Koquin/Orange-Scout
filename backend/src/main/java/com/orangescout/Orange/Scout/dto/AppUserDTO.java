package com.orangescout.Orange.Scout.dto;

import com.orangescout.Orange.Scout.model.AppUser;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AppUserDTO {
    private Long id;

    @NotBlank(message = "O email não pode estar em branco.")
    @Email(message = "Formato de email inválido.")
    @Size(max = 100, message = "O email deve ter no máximo 100 caracteres.")
    private String email;

    @NotBlank(message = "O nome de usuário não pode estar em branco.")
    @Size(min = 3, max = 50, message = "O nome de usuário deve ter entre 3 e 50 caracteres.")
    private String username;

    @NotNull(message = "O status de validação não pode ser nulo.")
    private boolean validated;

    @NotNull(message = "O status premium não pode ser nulo.")
    private boolean premium;

    @NotBlank(message = "A role não pode estar em branco.")
    private String role;

    public AppUserDTO() {}

    public AppUserDTO(Long id, String email, String username, boolean validated, boolean premium, String role) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.validated = validated;
        this.premium = premium;
        this.role = role;
    }

    public static AppUserDTO fromEntity(AppUser appUser) {
        return new AppUserDTO(
                appUser.getId(),
                appUser.getEmail(),
                appUser.getAppUsername(),
                appUser.isValidated(),
                appUser.isPremium(),
                appUser.getRole()
        );
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public boolean isValidated() { return validated; }
    public void setValidated(boolean validated) { this.validated = validated; }
    public boolean isPremium() { return premium; }
    public void setPremium(boolean premium) { this.premium = premium; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}