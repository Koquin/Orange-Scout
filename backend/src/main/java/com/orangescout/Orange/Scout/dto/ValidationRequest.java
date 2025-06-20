package com.orangescout.Orange.Scout.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ValidationRequest {

    @NotBlank(message = "O código de validação não pode estar em branco.")
    @Size(min = 6, max = 6, message = "O código de validação deve ter 6 dígitos.")
    private String code;

    public ValidationRequest() {
    }

    public ValidationRequest(String code) {
        this.code = code;
    }

    // Getters e Setters
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "ValidationRequest{" +
                "code='" + code + '\'' +
                '}';
    }
}