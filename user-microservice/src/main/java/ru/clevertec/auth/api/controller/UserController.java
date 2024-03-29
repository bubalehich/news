package ru.clevertec.auth.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.clevertec.auth.api.UserApi;
import ru.clevertec.auth.mapper.UserMapper;
import ru.clevertec.auth.model.TokenModel;
import ru.clevertec.auth.model.TokenValidationModel;
import ru.clevertec.auth.model.UserAuthModel;
import ru.clevertec.auth.model.UserMutationModel;
import ru.clevertec.auth.model.UserModel;
import ru.clevertec.auth.security.SecurityService;
import ru.clevertec.auth.service.UserService;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {
    private final UserService userService;

    private final SecurityService securityService;

    private final UserMapper mapper;

    @Override
    public ResponseEntity<UserModel> register(@RequestBody UserMutationModel userMutationModel) {
        var user = userService.register(userMutationModel.getEmail(), userMutationModel.getUsername(),
                userMutationModel.getPassword(), userMutationModel.getRoleName());
        var userViewModel = mapper.userToUserViewModel(user);

        var uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(userViewModel.getId())
                .toUri();

        return ResponseEntity.created(uri).body(userViewModel);
    }

    @Override
    public ResponseEntity<TokenModel> authenticate(@RequestBody UserAuthModel model) {
        var user = userService.authenticate(model.getEmail(), model.getPassword());
        var token = securityService.generateToken(user);

        return ResponseEntity.ok(token);
    }

    @Override
    public ResponseEntity<TokenValidationModel> validateToken(String token) {
        var response = securityService.validate(token);

        return ResponseEntity.ok(response);
    }
}