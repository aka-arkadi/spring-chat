package de.akadd.springchat.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class GoodUserDetails implements UserDetails {

    private String name;
    private String password;
    private List<GrantedAuthority> authorities;

    public GoodUserDetails(User user){
        this.name = user.getUserName();
        this.password = user.getPassword();
        this.authorities = new ArrayList();
        if (user.getRole().equals("USER")) {
            this.authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }
        if (user.getRole().equals("ADMIN")){
            this.authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            this.authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
    }

    public GoodUserDetails(){
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return name;
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
