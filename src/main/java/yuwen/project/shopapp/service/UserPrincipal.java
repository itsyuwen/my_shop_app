package yuwen.project.shopapp.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import yuwen.project.shopapp.domain.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class UserPrincipal implements UserDetails {
    private User user;
    private final Set<GrantedAuthority> authorities;


    public UserPrincipal(yuwen.project.shopapp.domain.User user, Set<GrantedAuthority> authorities) {
        this.user = user;
        this.authorities = authorities;
    }

    public Long getId() {
        return user.getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        int roleValue = user.getRole();

        // Map the role value to the corresponding authority
        if (roleValue == 1) {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        } else if (roleValue == 2) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        return authorities;
    }
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
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
}