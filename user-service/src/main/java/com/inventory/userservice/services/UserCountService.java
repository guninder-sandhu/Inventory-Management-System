package com.inventory.userservice.services;

import org.springframework.stereotype.Service;

@Service
public interface UserCountService {

    int getUserCountFromId();
    void updateUserCount(int userCount);
}
