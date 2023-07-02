package ru.clevertec.news.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import ru.clevertec.news.model.user.TokenModel;
import ru.clevertec.news.model.user.TokenValidationModel;
import ru.clevertec.news.model.user.UserAuthModel;
import ru.clevertec.news.model.user.UserModel;
import ru.clevertec.news.model.user.UserMutationModel;

@FeignClient(
        name = "UserClient",
        url = "${users.url}")
public interface UserClient {
    @PostMapping("/register")
    UserModel register(@RequestBody UserMutationModel model);

    @PostMapping("/authenticate")
    TokenModel authenticate(@RequestBody UserAuthModel model);

    @PostMapping("/validate")
    TokenValidationModel validateToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String token);
}
