package com.kamalteja.brevify.user.service.impl;

import com.kamalteja.brevify.security.util.JwtUtil;
import com.kamalteja.brevify.user.dao.IUserDAO;
import com.kamalteja.brevify.user.exception.AuthenticationFailedException;
import com.kamalteja.brevify.user.exception.UserExistsException;
import com.kamalteja.brevify.user.exception.UserNotFoundException;
import com.kamalteja.brevify.user.model.User;
import com.kamalteja.brevify.user.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.kamalteja.brevify.security.constants.SecurityConstants.TOKEN;

@Service
public class UserServiceImpl implements UserService {

    private final IUserDAO usersDAO;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserServiceImpl(IUserDAO usersDAO, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.usersDAO = usersDAO;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Registers a new user with the provided username and password.
     * Checks if the username already exists, and if so, throws a UserExistsException.
     * Creates a new user, encrypts the password, saves the user, and generates a JWT token.
     *
     * @param username the username for the new user
     * @param password the password for the new user
     * @return a Map containing the JWT token with key "token"
     * @throws UserExistsException if a user with the given username already exists
     */
    @Override
    public Map<String, String> register(String username, String password) {
        if (usersDAO.findByUsername(username) != null) {
            throw new UserExistsException();
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));

        usersDAO.save(user);
        String token = jwtUtil.generateToken(user.getUsername());
        Map<String, String> response = new HashMap<>();
        response.put(TOKEN, token);
        return response;
    }

    /**
     * Implementation of the UserService interface that provides user management functionality.
     * Handles user registration, authentication, and retrieval operations.
     * Uses password encryption for security and JWT tokens for authentication.
     * Throws appropriate exceptions for authentication failures and user existence issues.
     */
    @Override
    public Map<String, String> authenticateAndGenerateToken(String username, String password) {
        User user = authenticate(username, password);
        String token = jwtUtil.generateToken(user.getUsername());
        Map<String, String> response = new HashMap<>();
        response.put(TOKEN, token);
        return response;
    }

    private User authenticate(String username, String password) {
        User user = usersDAO.findByUsername(username);
        if (user == null) {
            throw new UserNotFoundException();
        } else if (passwordEncoder.matches(password, user.getPassword())) {
            return user;
        } else {
            throw new AuthenticationFailedException();
        }
    }

    @Override
    public User getUserByUsername(String username) {
        return usersDAO.findByUsername(username);
    }
}
