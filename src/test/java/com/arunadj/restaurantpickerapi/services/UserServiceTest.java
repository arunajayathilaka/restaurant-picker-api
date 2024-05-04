package com.arunadj.restaurantpickerapi.services;

import com.arunadj.restaurantpickerapi.dtos.UserDTO;
import com.arunadj.restaurantpickerapi.entities.User;
import com.arunadj.restaurantpickerapi.exceptions.UserAlreadyFoundException;
import com.arunadj.restaurantpickerapi.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    public UserServiceTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateUserWhenSuccessReturnUser() {
        UserDTO userDTO = new UserDTO("testUser", "password");
        when(userRepository.existsByUsername(userDTO.getUsername())).thenReturn(false);
        when(passwordEncoder.encode(userDTO.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any())).thenReturn(User.builder().username(userDTO.getUsername()).password("encodedPassword").build());

        User createdUser = userService.createUser(userDTO);

        assertNotNull(createdUser);
        assertEquals(userDTO.getUsername(), createdUser.getUsername());
        assertEquals("encodedPassword", createdUser.getPassword());
    }

    @Test
    public void testCreateUserWhenUserAlreadyExistsThrowEx() {
        UserDTO userDTO = new UserDTO("existingUser", "password");
        when(userRepository.existsByUsername(userDTO.getUsername())).thenReturn(true);

        assertThrows(UserAlreadyFoundException.class, () -> userService.createUser(userDTO));
    }
}
