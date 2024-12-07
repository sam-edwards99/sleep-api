package com.noom.interview.fullstack.sleep.service;

import com.noom.interview.fullstack.sleep.dto.UserDTO;
import com.noom.interview.fullstack.sleep.entity.User;
import com.noom.interview.fullstack.sleep.exception.UserNotFoundException;
import com.noom.interview.fullstack.sleep.mapper.UserMapper;
import com.noom.interview.fullstack.sleep.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(@Autowired UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public UserDTO createUser(UserDTO userDTO) {
        return UserMapper.toDto(userRepository.save(UserMapper.toEntity(userDTO)));
    }

    public UserDTO getUserById(Long id){
        return UserMapper.toDto(userRepository.findById(id).orElseThrow(UserNotFoundException::new));
    }

    public UserDTO updateUserById(Long id, UserDTO user) {
        User existingUser = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        existingUser.setName(user.getName());
        return UserMapper.toDto(userRepository.save(existingUser));
    }

    public void deleteUserById(Long id){
        if(userRepository.existsById(id)){
            userRepository.deleteById(id);
        }
        else{
            throw new UserNotFoundException();
        }
    }

}
