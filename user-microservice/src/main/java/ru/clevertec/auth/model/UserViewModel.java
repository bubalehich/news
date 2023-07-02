package ru.clevertec.auth.model;

import lombok.Data;

import java.util.UUID;

@Data
public class UserViewModel {
    private UUID id;
    private String username;
    private String email;
    private boolean isActive;
}
