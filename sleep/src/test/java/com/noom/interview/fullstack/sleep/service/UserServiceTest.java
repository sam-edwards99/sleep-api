package com.noom.interview.fullstack.sleep.service;

import com.noom.interview.fullstack.sleep.entity.User;
import com.noom.interview.fullstack.sleep.exception.UserNotFoundException;
import com.noom.interview.fullstack.sleep.mapper.UserMapper;
import com.noom.interview.fullstack.sleep.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static com.noom.interview.fullstack.sleep.SleepApplication.UNIT_TEST_PROFILE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles(UNIT_TEST_PROFILE)
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Test
    void getUserByIdShouldReturnUserDTO() {
        User user = User.builder().id(1L).name("Bob").build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertEquals(UserMapper.toDto(user), userService.getUserById(1L));
    }

    @Test
    void getUserByIdShouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    void createUserShouldReturnUserDTO() {
        User user = User.builder().id(1L).name("Bob").build();

        when(userRepository.save(any(User.class))).thenReturn(user);

        assertEquals(UserMapper.toDto(user), userService.createUser(UserMapper.toDto(user)));
    }

    @Test
    void updateUserByIdShouldReturnUserDTO() {
        User user = User.builder().id(1L).name("Bob").build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        assertEquals(UserMapper.toDto(user), userService.updateUserById(1L, UserMapper.toDto(user)));
    }

    @Test
    void updateUserByIdShouldThrowExceptionWhenUserNotFound() {
        User user = User.builder().id(1L).name("Bob").build();

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateUserById(1L, UserMapper.toDto(user)));
    }

    @Test
    void deleteUserByIdShouldSucceedIfUserExists() {
        doNothing().when(userRepository).deleteById(anyLong());
        when(userRepository.existsById(1L)).thenReturn(true);
        userService.deleteUserById(1L);

        verify(userRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void deleteUserByIdShouldThrowExceptionWhenUserNotFound() {
        when(userRepository.existsById(1L)).thenReturn(false);
        assertThrows(UserNotFoundException.class, () -> userService.deleteUserById(1L));
    }

}
