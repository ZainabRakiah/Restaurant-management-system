package com.restaurant.user.service;

import com.restaurant.common.dto.UserRequest;
import com.restaurant.user.model.User;
import com.restaurant.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void createUser_shouldPersistCustomerByDefault() {
        UserRequest request = new UserRequest("Alice", "alice@example.com", "secret1", "555-0100", null);
        User saved = new User();
        saved.setId(1L);
        saved.setName("Alice");
        saved.setEmail("alice@example.com");
        saved.setPhone("555-0100");
        saved.setRole("CUSTOMER");

        when(userRepository.existsByEmail("alice@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(saved);

        var response = userService.createUser(request);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.role()).isEqualTo("CUSTOMER");
    }

    @Test
    void createUser_shouldRejectDuplicateEmail() {
        UserRequest request = new UserRequest("Alice", "alice@example.com", "secret1", null, null);
        when(userRepository.existsByEmail("alice@example.com")).thenReturn(true);

        assertThatThrownBy(() -> userService.createUser(request))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Email already registered");

        verify(userRepository, never()).save(any());
    }

    @Test
    void getUser_shouldReturnUserWhenFound() {
        User user = new User();
        user.setId(2L);
        user.setName("Bob");
        user.setEmail("bob@example.com");
        user.setRole("DRIVER");

        when(userRepository.findById(2L)).thenReturn(Optional.of(user));

        var response = userService.getUser(2L);

        assertThat(response.name()).isEqualTo("Bob");
        assertThat(response.role()).isEqualTo("DRIVER");
    }
}
