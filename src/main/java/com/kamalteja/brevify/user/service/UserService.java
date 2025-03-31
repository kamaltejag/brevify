package com.kamalteja.brevify.user.service;

import com.kamalteja.brevify.user.model.User;

import java.util.Map;

public interface UserService {
    Map<String, String> register(String username, String password);

    Map<String, String> authenticateAndGenerateToken(String username, String password);

    User getUserByUsername(String username);
}
