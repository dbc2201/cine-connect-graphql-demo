package io.github.dbc2201.cineconnectgraphqldemo.graphql;

import io.github.dbc2201.cineconnectgraphqldemo.graphql.input.LoginInput;
import io.github.dbc2201.cineconnectgraphqldemo.graphql.input.RegisterInput;
import io.github.dbc2201.cineconnectgraphqldemo.graphql.type.AuthPayload;
import io.github.dbc2201.cineconnectgraphqldemo.service.AuthService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

/**
 * GraphQL controller for authentication mutations.
 * Handles user registration, login, and token refresh.
 */
@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @MutationMapping
    public AuthPayload register(@Argument RegisterInput input) {
        AuthService.AuthResult result = authService.register(
            input.username(),
            input.email(),
            input.password(),
            input.displayName()
        );

        return toAuthPayload(result);
    }

    @MutationMapping
    public AuthPayload login(@Argument LoginInput input) {
        AuthService.AuthResult result = authService.login(
            input.usernameOrEmail(),
            input.password()
        );

        return toAuthPayload(result);
    }

    @MutationMapping
    public AuthPayload refreshToken(@Argument String refreshToken) {
        AuthService.AuthResult result = authService.refreshToken(refreshToken);

        return toAuthPayload(result);
    }

    private AuthPayload toAuthPayload(AuthService.AuthResult result) {
        return new AuthPayload(
            result.accessToken(),
            result.refreshToken(),
            result.user()
        );
    }
}
