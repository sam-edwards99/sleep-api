package com.noom.interview.fullstack.sleep.mapper;

import com.noom.interview.fullstack.sleep.dto.UserDTO;
import com.noom.interview.fullstack.sleep.entity.User;


// Helper class for converting entities to DTOs and vice versa
//      It wasn't strictly necessary to have both DTOs and Entities for this class for this small application
//      but felt it was better practice to keep them separate as this allows the different layers of the application to be
//      more loosely coupled

public class UserMapper {

    public static UserDTO toDto(User entity) {
        return UserDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

    public static User toEntity(UserDTO dto) {
        return User.builder()
                .id(dto.getId())
                .name(dto.getName())
                .build();
    }
}
