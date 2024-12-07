package com.noom.interview.fullstack.sleep.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.noom.interview.fullstack.sleep.dto.UserDTO;
import com.noom.interview.fullstack.sleep.exception.UserNotFoundException;
import com.noom.interview.fullstack.sleep.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService service;

    @Test
    public void getUserShouldReturn200() throws Exception {
        UserDTO userDTO = UserDTO.builder().id(1L).name("bob").build();

        when(service.getUserById(anyLong())).thenReturn(userDTO);

        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/user/1"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        assertEquals(objectMapper.writeValueAsString(userDTO), result.getResponse().getContentAsString());
    }

    @Test
    public void getUserShouldReturn404IfUserDoesNotExist() throws Exception {
        when(service.getUserById(anyLong())).thenThrow(UserNotFoundException.class);

        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/user/1"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void createUserShouldReturn200() throws Exception {
        UserDTO userDTO = UserDTO.builder().id(1L).name("bob").build();

        String requestBody = objectMapper.writeValueAsString(userDTO);

        when(service.createUser(any(UserDTO.class))).thenReturn(userDTO);

        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/user")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        assertEquals(objectMapper.writeValueAsString(userDTO), result.getResponse().getContentAsString());
    }

    @Test
    public void updateUserShouldReturn200() throws Exception {
        UserDTO userDTO = UserDTO.builder().id(1L).name("updated bob").build();

        String requestBody = objectMapper.writeValueAsString(userDTO);

        when(service.updateUserById(anyLong(), any(UserDTO.class))).thenReturn(userDTO);

        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/user/1")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        assertEquals(objectMapper.writeValueAsString(userDTO), result.getResponse().getContentAsString());
    }

    @Test
    public void updateUserShouldReturn404IfUserDoesNotExist() throws Exception {
        UserDTO userDTO = UserDTO.builder().id(1L).name("updated bob").build();

        String requestBody = objectMapper.writeValueAsString(userDTO);

        when(service.updateUserById(anyLong(), any(UserDTO.class))).thenThrow(UserNotFoundException.class);

        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/user/1")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void deleteUserShouldReturn200() throws Exception {
        doNothing().when(service).deleteUserById(anyLong());

        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders
                        .delete("/user/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        assertEquals("User deleted", result.getResponse().getContentAsString());
    }

    @Test
    public void deleteUserShouldReturn404IfUserDoesNotExist() throws Exception {
        doThrow(UserNotFoundException.class).when(service).deleteUserById(anyLong());

        this.mockMvc.perform(MockMvcRequestBuilders
                        .delete("/user/1"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

}
