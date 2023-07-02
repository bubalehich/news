package ru.clevertec.news.model.user;

import java.util.Date;


public record TokenModel(Date expiredAt, String token, String type) {
}
