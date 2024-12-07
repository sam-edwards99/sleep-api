package com.noom.interview.fullstack.sleep.mapper;

import com.noom.interview.fullstack.sleep.dto.UserDTO;
import com.noom.interview.fullstack.sleep.entity.User;

public class UserMapper {

    public UserDTO toDto(User entity) {
        return UserDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

    public User toEntity(UserDTO dto) {
        return User.builder()
                .id(dto.getId())
                .name(dto.getName())
                .build();
    }
}
