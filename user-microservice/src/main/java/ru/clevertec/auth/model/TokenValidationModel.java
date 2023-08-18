package ru.clevertec.auth.model;

import lombok.Data;

@Data
public class TokenValidationModel {
    private boolean isValid;

    private String email;

    private String role;
}
