package ru.clevertec.news.api;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.clevertec.news.model.user.TokenModel;
import ru.clevertec.news.model.user.TokenValidationModel;
import ru.clevertec.news.model.user.UserAuthModel;
import ru.clevertec.news.model.user.UserModel;
import ru.clevertec.news.model.user.UserMutationModel;

@RequestMapping("auth")
@Validated
public interface AuthApi {
    @PostMapping("/register")
    ResponseEntity<UserModel> register(@RequestBody UserMutationModel model);

    @PostMapping("/authenticate")
    ResponseEntity<TokenModel> authenticate(@RequestBody UserAuthModel model);

    @PostMapping("/validate")
    ResponseEntity<TokenValidationModel> validateToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String token);
}
