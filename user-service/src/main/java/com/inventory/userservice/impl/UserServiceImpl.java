package com.inventory.userservice.impl;

import com.inventory.userservice.entities.User;
import com.inventory.userservice.exceptions.*;
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
        if (isUserEmailAndUserNameUnique(user)) {
            try {
                String uniqueUUID = UUID.randomUUID().toString();
                user.setUserId(uniqueUUID);
                int userCount = getUserCount();
                var userCode = generateUserCode(userCount);
                user.setUserCode(userCode);
                updateUserCount(userCount);
                return userRepository.save(user);
            } catch (Exception e) {
                throw new CreationException("Unable to create user");
            }
        } else {
            throw new CreationException("Unable to create user due to duplicate email or username");
        }
    }

    private boolean isUserEmailAndUserNameUnique(User user) {
        return (!userRepository.existsByEmail(user.getEmail()) && !userRepository.existsByUserName(user.getUserName()));
    }

    private int getUserCount() {
        return userCountService.getUserCountFromId();
    }

    public String generateUserCode(int userCount) {
        return String.format("EMP%05d", ++userCount);
    }

    @Transactional
    public void updateUserCount(int userCount) {
        try {
            userCountService.updateUserCount(++userCount);
        } catch (Exception e) {
            throw new UpdateException("Unable to update user count. " + e.getMessage());
        }
    }


    @Override
    public List<User> getAllUsers() {
        try {
            return userRepository.findAll();
        } catch (Exception e) {
            throw new RetrievalException("Unable to get all users. " + e.getMessage());
        }
    }

    @Override
    public User getUserById(String userId) {
        return userRepository.
                findById(userId).orElseThrow(() -> new NotFoundException("User not found " + userId));
    }

    @Override
    public User getUserByCode(String userCode) {
        try {
            var user = userRepository.findByUserCode(userCode);
            if (user == null) {
                throw new NotFoundException("User Not Fond " + userCode);
            }
            return user;
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RetrievalException("Unable to retrieve user " + userCode + ". " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void deleteUserById(String userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User not found " + userId);
        }
        try {
            userRepository.deleteById(userId);
        } catch (NotFoundException ne) {
            throw ne;
        } catch (Exception e) {
            throw new DeletionException("Unable to delete user " + userId + ". " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void deleteUserByCode(String userCode) {
        if (!userRepository.existsByUserCode(userCode)) {
            throw new NotFoundException("User not found " + userCode);
        }
        try {
            userRepository.deleteByUserCode(userCode);
        } catch (NotFoundException ne) {
            throw ne;
        } catch (Exception e) {
            throw new DeletionException("Unable to delete user " + userCode + ". " + e.getMessage());
        }

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
    @Transactional
    public void updateUserById(String userId, User user) {
        updateUser(userId, getUserById(userId), user);
    }

    private void updateUser(String identification, User retrievedUser, User updatedUser) {
        if (retrievedUser == null) {
            throw new NotFoundException("User not found " + identification);
        }
        try {
            updatedUser.setUserId(retrievedUser.getUserId());
            updatedUser.setUserCode(retrievedUser.getUserCode());
            userRepository.save(updatedUser);
        } catch (NotFoundException notFoundException) {
            throw notFoundException;
        } catch (Exception e) {
            throw new UpdateException("Unable to save updated user " + identification);
        }
    }

    @Override
    @Transactional
    public void updateUserByCode(String userCode, User user) {
        updateUser(userCode, getUserByCode(userCode), user);
    }

    @Override
    @Transactional
    public void deleteAllUsers() {
        try {
            userRepository.deleteAll();
        } catch (Exception e) {
            throw new DeletionException("Unable to delete all users. " + e.getMessage());
        }
    }
}
