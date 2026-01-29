package io.github.dbc2201.cineconnectgraphqldemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration for CineConnect.
 *
 * <p>This configuration establishes a stateless security model suitable for
 * a GraphQL API that will use JWT token-based authentication (Phase 4).
 *
 * <h3>Security Decisions:</h3>
 * <ul>
 *   <li><b>CSRF disabled:</b> This API is stateless and uses token-based auth.
 *       CSRF protection is unnecessary because we don't use cookies for auth.
 *       Tokens must be sent in Authorization header, which browsers don't auto-attach.</li>
 *   <li><b>Stateless sessions:</b> No server-side session state; each request
 *       must be independently authenticated via JWT token.</li>
 * </ul>
 *
 * <h3>Current State (Pre-Phase 4):</h3>
 * <p>All endpoints are currently open for development. Phase 4 will add:
 * <ul>
 *   <li>JWT token validation filter</li>
 *   <li>Role-based access control for mutations</li>
 *   <li>User authentication endpoints</li>
 * </ul>
 *
 * @see <a href="https://owasp.org/www-community/attacks/csrf">OWASP CSRF</a>
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // CSRF disabled: This is a stateless API using token-based auth (not cookies).
            // Tokens are sent via Authorization header which browsers don't auto-attach,
            // making CSRF attacks infeasible. See class javadoc for details.
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // TODO: Phase 4 - Replace permitAll() with proper authorization:
            // - Queries: mostly public (except user-specific data)
            // - Mutations: require authentication
            // - Admin operations: require ADMIN role
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/graphql/**", "/graphiql/**").permitAll()
                .anyRequest().permitAll()
            );

        return http.build();
    }
}
