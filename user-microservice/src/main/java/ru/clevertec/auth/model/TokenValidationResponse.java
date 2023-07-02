package ru.clevertec.auth.model;

import lombok.Data;

@Data
public class TokenValidationResponse {
    private boolean isValid;

    private String email;

    private String role;
}
