package ru.clevertec.auth.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.clevertec.auth.model.TokenDto;
import ru.clevertec.auth.model.TokenValidationResponse;
import ru.clevertec.auth.model.UserAuthModel;
import ru.clevertec.auth.model.UserMutationModel;
import ru.clevertec.auth.model.UserViewModel;

@Validated
@RequestMapping(value = "/api/v1/users", produces = "application/json")
public interface UserApi {
    @Operation(
            summary = "Register new user",
            tags = {"User"},
            description = "Create new user with unique email & unique username")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "User create successfully"),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Email and username should be unique",
                            content = @Content(mediaType = "application/json"))
            })
    @PostMapping("/register")
    ResponseEntity<UserViewModel> register(@Valid @RequestBody UserMutationModel userMutationModel);

    @Operation(
            summary = "Authenticate user by username & password",
            tags = {"User"},
            description = "Returns jwt token if user credentials are valid")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User authenticated successfully"),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid password or username")
            })
    @PostMapping("/authenticate")
    ResponseEntity<TokenDto> authenticate(@Valid @RequestBody UserAuthModel model);

    @Operation(
            summary = "Validate token",
            tags = {"Authentication", "User"},
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponses(
            value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Valid",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/validate")
    ResponseEntity<TokenValidationResponse> validateToken(@Parameter(hidden = true) String token);
}
