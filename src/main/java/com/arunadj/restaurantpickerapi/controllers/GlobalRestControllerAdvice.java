package com.arunadj.restaurantpickerapi.controllers;

import com.arunadj.restaurantpickerapi.exceptions.NORestaurantException;
import com.arunadj.restaurantpickerapi.exceptions.UserAlreadyFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalRestControllerAdvice {

    @ExceptionHandler(UserAlreadyFoundException.class)
    public ResponseEntity<String> handleCustomException(UserAlreadyFoundException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(NORestaurantException.class)
    public ResponseEntity<String> handleCustomException(NORestaurantException e) {
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(e.getMessage());
    }
}
