package yuwen.project.shopapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import yuwen.project.shopapp.repository.UserRepository;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

class RoleConstants {
    public static final int ROLE_USER = 1; // Buyer
    public static final int ROLE_ADMIN = 2; // Seller
}
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        yuwen.project.shopapp.domain.User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        Set<GrantedAuthority> authorities = new HashSet<>();
        int roleValue = user.getRole();

        switch (roleValue) {
            case RoleConstants.ROLE_USER:
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                break;
            case RoleConstants.ROLE_ADMIN:
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                break;
            default:
                throw new IllegalArgumentException("Unknown role value: " + roleValue);
        }

        return new UserPrincipal(user, authorities);
    }
}