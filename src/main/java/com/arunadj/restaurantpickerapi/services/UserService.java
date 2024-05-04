package com.arunadj.restaurantpickerapi.services;

import com.arunadj.restaurantpickerapi.dtos.UserDTO;
import com.arunadj.restaurantpickerapi.entities.User;
import com.arunadj.restaurantpickerapi.exceptions.UserAlreadyFoundException;
import com.arunadj.restaurantpickerapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User createUser(UserDTO userDTO) {
        boolean existsByUsername = userRepository.existsByUsername(userDTO.getUsername());
        if (existsByUsername) {
            throw new UserAlreadyFoundException("User already there");
        }
        return userRepository.save(User.builder().username(userDTO.getUsername()).password(passwordEncoder.encode(userDTO.getPassword())).build());
    }
}
