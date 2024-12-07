package com.noom.interview.fullstack.sleep.service;

import com.noom.interview.fullstack.sleep.entity.User;
import com.noom.interview.fullstack.sleep.exception.UserNotFoundException;
import com.noom.interview.fullstack.sleep.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(@Autowired UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User getUserById(Long id){
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    public User updateUserById(Long id, User user) {
        User existingUser = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        existingUser.setName(user.getName());
        return userRepository.save(existingUser);
    }

    public void deleteUserById(Long id){
        userRepository.deleteById(id);
    }

}
