package ru.clevertec.auth.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateModel {
    @NotBlank
    @Size(min = 4, max = 50)
    private String oldPassword;

    @NotBlank
    @Size(min = 4, max = 50)
    private String newPassword;
}
