package ru.clevertec.auth.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserMutationModel {
    @NotBlank
    private String email;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String roleName;
}
