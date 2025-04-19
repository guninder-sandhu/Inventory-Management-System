package com.inventory.userservice.impl;

import com.inventory.userservice.entities.User;
import com.inventory.userservice.exceptions.CreationException;
import com.inventory.userservice.exceptions.UserNotFoundException;
import com.inventory.userservice.repositories.UserCountRepository;
import com.inventory.userservice.repositories.UserRepository;
import com.inventory.userservice.services.UserCountService;
import com.inventory.userservice.services.UserServices;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserServices {

    private final UserRepository userRepository;
    private final UserCountService userCountService;

    UserServiceImpl(UserRepository userRepository, UserCountService userCountService) {
        this.userRepository = userRepository;
        this.userCountService = userCountService;
    }

    @Override
    @Transactional
    public User saveUser(User user) {
        if(isUserEmailAndUserNameUsinque(user)){
            try{
                String uniqueUUID = UUID.randomUUID().toString();
                user.setUserId(uniqueUUID);
                int userCount = getUserCount();
                var userCode = generateUserCode(userCount);
                user.setUserCode(userCode);
                updateUserCount(userCount);
                return userRepository.save(user);
            } catch (Exception e){
                throw new CreationException("Unable to create user");
            }
        }else{
            throw new CreationException("Unable to create user due to duplicate email or username");
        }

    }

    private boolean isUserEmailAndUserNameUsinque(User user) {
        return(!userRepository.existsByEmail(user.getEmail())&& !userRepository.existsByUserName(user.getUserName()));
    }

    private int getUserCount() {
        return userCountService.getUserCountFromId();
    }

    public String generateUserCode(int userCount) {
        return String.format("EMP%05d", ++userCount);
    }

    public void updateUserCount(int userCount) {
        userCountService.updateUserCount(++userCount);
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
