package com.noom.interview.fullstack.sleep.service;

import com.noom.interview.fullstack.sleep.entity.User;
import com.noom.interview.fullstack.sleep.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User getUserById(Long id){
        return userRepository.findById(id).get();
    }

    public User updateUserById(Long id, User user) {
        User existingUser = userRepository.findById(id).get();
        existingUser.setName(user.getName());
        return userRepository.save(existingUser);
    }

    public void deleteUserById(Long id){
        userRepository.deleteById(id);
    }





}
