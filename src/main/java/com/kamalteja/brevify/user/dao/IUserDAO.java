package com.kamalteja.brevify.user.dao;

import com.kamalteja.brevify.user.model.User;

public interface IUserDAO {
    User save(User user);

    User findByUsername(String username);
}
