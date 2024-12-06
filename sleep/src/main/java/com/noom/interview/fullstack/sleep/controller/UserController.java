package com.noom.interview.fullstack.sleep.controller;

import com.noom.interview.fullstack.sleep.entity.User;
import com.noom.interview.fullstack.sleep.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    // Get a user by id
    @GetMapping("/user/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    // Create a User
    @PostMapping("/user")
    public User createNewUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    // Update a User by Id
    @PutMapping("/user/{id}")
    public User updateUserById(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUserById(id, user);
    }

    // Delete a User By Id
    @DeleteMapping("/user/{id}")
    public String deleteUserByID(@PathVariable Long id) {
        userService.deleteUserById(id);
        return "User deleted";
    }
}

