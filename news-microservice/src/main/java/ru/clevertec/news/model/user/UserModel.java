package ru.clevertec.news.model.user;

import lombok.Data;

import java.util.UUID;

@Data
public class UserModel {
    private UUID id;
    private String username;
    private String email;
    private boolean isActive;
}
