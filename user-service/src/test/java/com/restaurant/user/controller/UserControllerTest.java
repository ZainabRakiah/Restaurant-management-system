package com.restaurant.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurant.common.dto.UserRequest;
import com.restaurant.common.dto.UserResponse;
import com.restaurant.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    void createUser_shouldReturn201() throws Exception {
        UserRequest request = new UserRequest("Alice", "alice@example.com", "secret1", "555-0100", "CUSTOMER");
        when(userService.createUser(any())).thenReturn(
                new UserResponse(1L, "Alice", "alice@example.com", "555-0100", "CUSTOMER"));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("alice@example.com"));
    }

    @Test
    void getUser_shouldReturnUser() throws Exception {
        when(userService.getUser(1L)).thenReturn(
                new UserResponse(1L, "Alice", "alice@example.com", "555-0100", "CUSTOMER"));

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice"));
    }
}
