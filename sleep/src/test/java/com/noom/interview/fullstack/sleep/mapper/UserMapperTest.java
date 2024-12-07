package com.noom.interview.fullstack.sleep.mapper;

import com.noom.interview.fullstack.sleep.dto.UserDTO;
import com.noom.interview.fullstack.sleep.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static com.noom.interview.fullstack.sleep.SleepApplication.UNIT_TEST_PROFILE;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles(UNIT_TEST_PROFILE)
public class UserMapperTest {
    @Test
    public void shouldMapUserEntityToDto(){
        User user = User
                .builder()
                .id(1L)
                .name("bob")
                .build();

        UserDTO userDTO = UserDTO
                .builder()
                .id(1L)
                .name("bob")
                .build();

        assertEquals(userDTO, UserMapper.toDto(user));
    }

    @Test
    public void shouldMapUserDtoToEntity(){
        User user = User
                .builder()
                .id(1L)
                .name("bob")
                .build();

        UserDTO userDTO = UserDTO
                .builder()
                .id(1L)
                .name("bob")
                .build();

        assertEquals(user, UserMapper.toEntity(userDTO));
    }
}
