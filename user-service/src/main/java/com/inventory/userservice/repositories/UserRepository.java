package com.inventory.userservice.repositories;

import com.inventory.userservice.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

    boolean existsByEmail(String email);

    boolean existsByUserName(String username);

    boolean existsByUserCode(String userCode);

    void deleteByUserCode(String userCode);

    User findByUserCode(String userCode);
}
