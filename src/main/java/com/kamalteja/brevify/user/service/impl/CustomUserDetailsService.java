package com.kamalteja.brevify.user.service.impl;

import com.kamalteja.brevify.user.dao.IUserDAO;
import com.kamalteja.brevify.user.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Implementation of UserDetailsService that loads user details from the database.
 * This service is used by Spring Security for authentication and authorization.
 * It retrieves user information using IUserDAO and converts it to Spring Security's UserDetails.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final IUserDAO usersDAO;

    public CustomUserDetailsService(IUserDAO usersDAO) {
        this.usersDAO = usersDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = usersDAO.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.emptyList()
        );
    }
}
