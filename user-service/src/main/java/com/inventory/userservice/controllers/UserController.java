package com.inventory.userservice.controllers;


import com.inventory.userservice.entities.User;
import com.inventory.userservice.services.UserServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserServices userService;


    UserController(UserServices userServices) {
        this.userService = userServices;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        userService.saveUser(user);
        return new ResponseEntity<User>(user, HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(@PathVariable String userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping("/update/{userId}")
    public ResponseEntity<Map<String, Object>> updateUser(@PathVariable String userId, @RequestBody User user) {
        var currentUser = userService.getUserById(userId);
        if (currentUser != null) {
            user.setUserId(userId);
            userService.updateUserById(userId, user);
            Map<String, Object> response = new HashMap<>();
            response.put("user", user);
            response.put("message", "User updated successfully");
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @PostMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable String userId) {
        userService.deleteUserById(userId);
        if (userService.existsUserById(userId)) {
            return ResponseEntity.ok("User deleted successfully");
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unable to delete User.Either user doesnt exist or no permission");

    }

}
