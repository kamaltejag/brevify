package com.kamalteja.brevify.user.dao.impl;

import com.kamalteja.brevify.user.dao.IUserDAO;
import com.kamalteja.brevify.user.model.User;
import com.kamalteja.brevify.user.repository.UsersRepository;
import org.springframework.stereotype.Service;

@Service
public class UserDAOImpl implements IUserDAO {

    private final UsersRepository usersRepository;

    public UserDAOImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public User save(User user) {
        return usersRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return usersRepository.findByUsername(username).orElse(null);
    }
}
