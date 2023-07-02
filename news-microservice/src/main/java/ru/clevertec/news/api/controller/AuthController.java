package ru.clevertec.news.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.news.api.AuthApi;
import ru.clevertec.news.api.UserClient;
import ru.clevertec.news.model.user.TokenModel;
import ru.clevertec.news.model.user.TokenValidationModel;
import ru.clevertec.news.model.user.UserAuthModel;
import ru.clevertec.news.model.user.UserModel;
import ru.clevertec.news.model.user.UserMutationModel;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {
    private final UserClient client;

    @Override
    public ResponseEntity<UserModel> register(UserMutationModel model) {
        return ResponseEntity.ok(client.register(model));
    }

    @Override
    public ResponseEntity<TokenModel> authenticate(UserAuthModel model) {
        return ResponseEntity.ok(client.authenticate(model));
    }

    @Override
    public ResponseEntity<TokenValidationModel> validateToken(String token) {
        return ResponseEntity.ok(client.validateToken(token));
    }
}
