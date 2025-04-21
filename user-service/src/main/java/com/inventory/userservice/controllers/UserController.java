package com.inventory.userservice.controllers;


import com.inventory.userservice.entities.User;
import com.inventory.userservice.response.ApiResponse;
import com.inventory.userservice.services.UserServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserServices service;


    UserController(UserServices userServices) {
        this.service = userServices;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<User>> createUser(@RequestBody User user) {
        ApiResponse<User> response = new ApiResponse<>(
                "User created successfully",
                HttpStatus.CREATED.value(),
                LocalDateTime.now(),
                service.saveUser(user));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<User>> getUser(@PathVariable String userId) {
        ApiResponse<User> response = new ApiResponse<>(
                "User retrieved successfully",
                HttpStatus.OK.value(),
                LocalDateTime.now(),
                service.getUserById(userId));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        ApiResponse<List<User>> response = new ApiResponse<>(
                "All Users retrieved successfully",
                HttpStatus.OK.value(),
                LocalDateTime.now(),
                service.getAllUsers());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/update/{userId}")
    public ResponseEntity<ApiResponse<Void>> updateUser(@PathVariable String userId, @RequestBody User user) {
        user.setUserId(userId);
        service.updateUserById(userId, user);
        ApiResponse<Void> response = new ApiResponse<>(
                "User updated successfully",
                HttpStatus.OK.value(),
                LocalDateTime.now(),
                null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/delete/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable String userId) {
        service.deleteUserById(userId);
        ApiResponse<Void> response = new ApiResponse<>(
                "User deleted successfully",
                HttpStatus.OK.value(),
                LocalDateTime.now(),
                null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/update/code/{userCode}")
    public ResponseEntity<ApiResponse<Void>> updateUserByCode(@PathVariable String userCode, @RequestBody User user) {
        service.updateUserByCode(userCode, user);
        ApiResponse<Void> response = new ApiResponse<>(
                "User updated successfully",
                HttpStatus.OK.value(),
                LocalDateTime.now(),
                null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/delete/code/{userCode}")
    public ResponseEntity<ApiResponse<Void>> deleteUserByCode(@PathVariable String userCode) {
        service.deleteUserByCode(userCode);
        ApiResponse<Void> response = new ApiResponse<>(
                "User deleted successfully",
                HttpStatus.OK.value(),
                LocalDateTime.now(),
                null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
