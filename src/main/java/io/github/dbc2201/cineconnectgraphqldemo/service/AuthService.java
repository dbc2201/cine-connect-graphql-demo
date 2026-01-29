package io.github.dbc2201.cineconnectgraphqldemo.service;

import io.github.dbc2201.cineconnectgraphqldemo.domain.User;
import io.github.dbc2201.cineconnectgraphqldemo.repository.UserRepository;
import io.github.dbc2201.cineconnectgraphqldemo.security.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for authentication operations: register, login, refresh token.
 */
@Service
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Registers a new user and returns authentication tokens.
     *
     * @throws IllegalArgumentException if username or email already exists
     */
    @Transactional
    public AuthResult register(String username, String email, String password, String displayName) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already taken");
        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already registered");
        }

        String hashedPassword = passwordEncoder.encode(password);
        User user = new User(username, email, hashedPassword);
        if (displayName != null) {
            user.setDisplayName(displayName);
        }

        user = userRepository.save(user);

        return createAuthResult(user);
    }

    /**
     * Authenticates a user by username/email and password.
     *
     * @throws IllegalArgumentException if credentials are invalid
     */
    public AuthResult login(String usernameOrEmail, String password) {
        User user = userRepository.findByUsername(usernameOrEmail)
            .or(() -> userRepository.findByEmail(usernameOrEmail))
            .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        return createAuthResult(user);
    }

    /**
     * Refreshes an access token using a valid refresh token.
     *
     * @throws IllegalArgumentException if refresh token is invalid
     */
    public AuthResult refreshToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken) || !jwtTokenProvider.isRefreshToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        Long userId = jwtTokenProvider.getUserId(refreshToken);
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return createAuthResult(user);
    }

    private AuthResult createAuthResult(User user) {
        String accessToken = jwtTokenProvider.createAccessToken(user.getId(), user.getUsername());
        String newRefreshToken = jwtTokenProvider.createRefreshToken(user.getId());

        return new AuthResult(accessToken, newRefreshToken, user);
    }

    /**
     * Result of successful authentication containing tokens and user.
     */
    public record AuthResult(String accessToken, String refreshToken, User user) {}
}
