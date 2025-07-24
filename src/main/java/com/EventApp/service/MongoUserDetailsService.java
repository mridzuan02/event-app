package com.EventApp.service;

import com.EventApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import com.EventApp.model.User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class MongoUserDetailsService implements UserDetailsService
{
    @Autowired
    private UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        User user = userRepo.findByUsername(username);
        if (user == null)
            throw new UsernameNotFoundException("User not found");

        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(user.getRole()) // e.g., "ROLE_ADMIN"
        );

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
}
