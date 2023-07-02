package ru.clevertec.auth.model;

import java.util.Date;


public record TokenModel(Date expiredAt, String token, String type) {
}
