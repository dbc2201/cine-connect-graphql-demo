package io.github.dbc2201.cineconnectgraphqldemo.config;

import io.github.dbc2201.cineconnectgraphqldemo.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security configuration for CineConnect.
 *
 * <p>This configuration establishes a stateless security model suitable for
 * a GraphQL API that uses JWT token-based authentication.
 *
 * <h3>Security Decisions:</h3>
 * <ul>
 *   <li><b>CSRF disabled:</b> This API is stateless and uses token-based auth.
 *       CSRF protection is unnecessary because we don't use cookies for auth.
 *       Tokens must be sent in Authorization header, which browsers don't auto-attach.</li>
 *   <li><b>Stateless sessions:</b> No server-side session state; each request
 *       must be independently authenticated via JWT token.</li>
 *   <li><b>BCrypt:</b> Industry-standard password hashing with adaptive cost factor.</li>
 * </ul>
 *
 * <h3>Authorization Model:</h3>
 * <p>GraphQL handles authorization differently from REST. Since all requests go to /graphql,
 * we permit all requests at the HTTP level and handle authorization within GraphQL resolvers.
 * This allows fine-grained per-field authorization.
 *
 * @see <a href="https://owasp.org/www-community/attacks/csrf">OWASP CSRF</a>
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // CSRF disabled: This is a stateless API using token-based auth (not cookies).
            // Tokens are sent via Authorization header which browsers don't auto-attach,
            // making CSRF attacks infeasible. See class javadoc for details.
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // Add JWT filter before username/password authentication
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            // GraphQL authorization is handled in resolvers, permit all at HTTP level
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/graphql/**", "/graphiql/**").permitAll()
                .anyRequest().permitAll()
            );

        return http.build();
    }

    /**
     * BCrypt password encoder with default strength (10 rounds).
     * This provides strong, adaptive hashing that becomes slower as hardware improves.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
