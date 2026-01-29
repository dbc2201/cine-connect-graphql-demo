package io.github.dbc2201.cineconnectgraphqldemo.security;

import io.github.dbc2201.cineconnectgraphqldemo.domain.User;
import io.github.dbc2201.cineconnectgraphqldemo.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * UserDetailsService implementation that loads user data from the database.
 * Used by Spring Security for authentication.
 */
@Service
public class CineConnectUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CineConnectUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return new CineConnectUserPrincipal(user);
    }

    /**
     * Custom UserDetails implementation that wraps our User entity.
     */
    public record CineConnectUserPrincipal(User user) implements UserDetails {

        @Override
        public String getUsername() {
            return user.getUsername();
        }

        @Override
        public String getPassword() {
            return user.getPasswordHash();
        }

        @Override
        public List<SimpleGrantedAuthority> getAuthorities() {
            // For now, all users have USER role
            // Later we can add admin roles based on a role field
            return List.of(new SimpleGrantedAuthority("ROLE_USER"));
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }

        public Long getId() {
            return user.getId();
        }
    }
}
