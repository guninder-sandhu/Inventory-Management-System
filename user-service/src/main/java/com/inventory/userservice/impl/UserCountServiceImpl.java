package com.inventory.userservice.impl;

import com.inventory.userservice.entities.UserCount;
import com.inventory.userservice.exceptions.CreationException;
import com.inventory.userservice.repositories.UserCountRepository;
import com.inventory.userservice.services.UserCountService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserCountServiceImpl implements UserCountService {

    private final UserCountRepository repository;

    @PostConstruct
    public void init() {
        Optional<UserCount> userCount = repository.findById(1);
        if (userCount.isEmpty()) {
            try {
                UserCount freshCount = new UserCount(1,0);
                repository.save(freshCount);
            } catch (Exception e) {
                throw new CreationException("Unable to create User Count");
            }
        }
    }

    public UserCountServiceImpl(UserCountRepository repository) {
        this.repository = repository;
    }
    @Override
    public int getUserCountFromId() {
        return repository.getUserCount();
    }

    @Override
    public void updateUserCount(int userCount) {
           repository.updateUserCount(userCount);
    }
}
