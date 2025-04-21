package com.inventory.userservice.services;

import com.inventory.userservice.entities.User;

import java.util.List;

public interface UserServices {
    //user operations

    User saveUser(User user);

    List<User> getAllUsers();

    User getUserById(String userId);

    void deleteUserById(String userId);

    boolean existsUserById(String userId);

    boolean existsUserByEmail(String email);

    boolean existsUserByUsername(String username);

    void updateUserById(String userId, User user);

    void updateUserByCode(String userCode, User user);

    void deleteUserByCode(String userCode);

    User getUserByCode(String userCode);

    void deleteAllUsers();

}
