package bandwagon.bandwagonback.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AuthUserDetails implements UserDetails {
    private final String username; // Maps to User의 email (email로 로그인 하기 때문)
    private final String password;
    private final List<GrantedAuthority> authorities;

    public AuthUserDetails(User user) {
        this.username = user.getEmail();
        this.password = user.getPassword();
        this.authorities = new ArrayList<>();
    }

    public AuthUserDetails(String email){
        this.username = email;
        this.password = "";
        this.authorities = new ArrayList<>();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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
