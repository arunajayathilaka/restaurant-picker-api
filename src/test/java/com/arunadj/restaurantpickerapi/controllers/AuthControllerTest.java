package com.arunadj.restaurantpickerapi.controllers;

import com.arunadj.restaurantpickerapi.dtos.AuthResponseDTO;
import com.arunadj.restaurantpickerapi.dtos.UserDTO;
import com.arunadj.restaurantpickerapi.services.UserService;
import com.arunadj.restaurantpickerapi.utils.JwtTokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserService userService;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAuthenticateUserWhenSuccess() {
        UserDTO userDTO = new UserDTO("testUser", "password");
        User userDetails = new User(userDTO.getUsername(), userDTO.getPassword(), List.of());
        String token = "sampleToken";
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtTokenUtil.generateToken(userDetails)).thenReturn(token);

        ResponseEntity<AuthResponseDTO> responseEntity = authController.authenticateUser(userDTO);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(userDTO.getUsername(), responseEntity.getBody().getUsername());
        assertEquals(token, responseEntity.getBody().getToken());
    }

    @Test
    public void testAuthenticateUserWhenFailure() {
        UserDTO userDTO = new UserDTO("testUser", "wrongPassword");

        when(authenticationManager.authenticate(any())).thenThrow(BadCredentialsException.class);

        ResponseEntity<AuthResponseDTO> responseEntity = authController.authenticateUser(userDTO);

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }

    @Test
    public void testRegisterUserWhenSuccess() {
        UserDTO userDTO = new UserDTO("testUser", "password");

        ResponseEntity<?> responseEntity = authController.registerUser(userDTO);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(userService, times(1)).createUser(userDTO);
    }
}
