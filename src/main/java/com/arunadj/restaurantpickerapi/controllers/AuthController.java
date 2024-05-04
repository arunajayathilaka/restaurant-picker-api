package com.arunadj.restaurantpickerapi.controllers;


import com.arunadj.restaurantpickerapi.dtos.AuthResponseDTO;
import com.arunadj.restaurantpickerapi.dtos.DataResponseDTO;
import com.arunadj.restaurantpickerapi.dtos.UserDTO;
import com.arunadj.restaurantpickerapi.services.UserService;
import com.arunadj.restaurantpickerapi.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponseDTO> authenticateUser(@RequestBody UserDTO userDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    userDTO.getUsername(), userDTO.getPassword()));

            User user = (User) authentication.getPrincipal();

            return ResponseEntity.ok()
                    .body(AuthResponseDTO.builder()
                            .username(user.getUsername())
                            .token(jwtTokenUtil.generateToken(user))
                            .build());

        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {

        // create user object
        userService.createUser(userDTO);
        return new ResponseEntity<>(DataResponseDTO.builder().status("SUCCESS").message("User registered successfully").build(), HttpStatus.OK);

    }
}
