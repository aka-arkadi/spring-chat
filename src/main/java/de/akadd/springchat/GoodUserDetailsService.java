package de.akadd.springchat;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class GoodUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername (String s) throws UsernameNotFoundException {
        return new GoodUserDetails(s);
    }
}
