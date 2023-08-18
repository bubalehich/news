package ru.clevertec.auth.model;

import lombok.Data;

@Data
public class UserAuthModel {
    private String email;
    private String password;
}
