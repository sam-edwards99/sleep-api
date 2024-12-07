package com.noom.interview.fullstack.sleep.controller;

import com.noom.interview.fullstack.sleep.dto.UserDTO;
import com.noom.interview.fullstack.sleep.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    // Get a user by id
    @GetMapping("/user/{id}")
    public UserDTO getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    // Create a User
    @PostMapping("/user")
    public UserDTO createNewUser(@RequestBody UserDTO userDto) {
        return userService.createUser(userDto);
    }

    // Update a User by Id
    @PutMapping("/user/{id}")
    public UserDTO updateUserById(@PathVariable Long id, @RequestBody UserDTO userDto) {
        return userService.updateUserById(id, userDto);
    }

    // Delete a User By Id
    @DeleteMapping("/user/{id}")
    public String deleteUserByID(@PathVariable Long id) {
        userService.deleteUserById(id);
        return "User deleted";
    }
}

