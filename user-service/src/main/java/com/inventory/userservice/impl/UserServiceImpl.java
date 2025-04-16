package com.inventory.userservice.impl;

import com.inventory.userservice.entities.User;
import com.inventory.userservice.exceptions.UserNotFoundException;
import com.inventory.userservice.repositories.UserRepository;
import com.inventory.userservice.services.UserServices;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserServices {

    private final UserRepository userRepository;

    UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User saveUser(User user) {
        String uniqueUUID = UUID.randomUUID().toString();
        user.setUserId(uniqueUUID);
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(String userId) {
        return userRepository.
                findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Override
    public void deleteUserById(String userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public boolean existsUserById(String userId) {
        return userRepository.existsById(userId);
    }

    @Override
    public boolean existsUserByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsUserByUsername(String username) {
        return userRepository.existsByUserName(username);
    }

    @Override
    public void updateUserById(String userId, User user) {
        var userById = getUserById(userId);
        if (userById != null) {
            userRepository.save(user);
        }
    }
}
