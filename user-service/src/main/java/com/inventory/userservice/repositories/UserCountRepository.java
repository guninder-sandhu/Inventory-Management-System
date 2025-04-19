package com.inventory.userservice.repositories;

import com.inventory.userservice.entities.UserCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserCountRepository extends JpaRepository<UserCount, Integer> {

    @Modifying
    @Query("update UserCount uc SET uc.userCount= :usercount where uc.id=1")
    void updateUserCount(int usercount);

    @Query("select uc.userCount from UserCount uc where uc.id=1")
    int getUserCount();
}
