package ru.clevertec.auth.model;

import java.util.Date;


public record TokenDto(Date expiredAt, String token, String type) {
}
