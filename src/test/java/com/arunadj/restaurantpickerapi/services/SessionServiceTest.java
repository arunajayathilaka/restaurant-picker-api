package com.arunadj.restaurantpickerapi.services;

import com.arunadj.restaurantpickerapi.dtos.RestaurantDTO;
import com.arunadj.restaurantpickerapi.entities.Restaurant;
import com.arunadj.restaurantpickerapi.entities.Session;
import com.arunadj.restaurantpickerapi.entities.User;
import com.arunadj.restaurantpickerapi.exceptions.NORestaurantException;
import com.arunadj.restaurantpickerapi.repositories.SessionRepository;
import com.arunadj.restaurantpickerapi.repositories.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SessionServiceTest {

    @InjectMocks
    private SessionService sessionService;

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RestaurantService restaurantService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testJoinSessionWhenSessionIsThereReturnTrue() {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("aruna");
        SecurityContextHolder.setContext(securityContext);

        when(this.userRepository.findByUsername("aruna")).thenReturn(Optional.of(User.builder().username("aruna").build()));
        when(this.sessionRepository.findById(1L)).thenReturn(Optional.of(Session.builder().users(new ArrayList<>()).build()));

        boolean joined = this.sessionService.joinSession(1L);

        verify(this.sessionRepository, times(1)).save(any());
        Assertions.assertThat(joined).isTrue();
    }

    @Test
    public void testJoinSessionWhenSessionNotIsThereReturnFalse() {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("aruna");
        SecurityContextHolder.setContext(securityContext);

        when(this.userRepository.findByUsername("aruna")).thenReturn(Optional.of(User.builder().username("aruna").build()));
        when(this.sessionRepository.findById(1L)).thenReturn(Optional.empty());

        boolean joined = this.sessionService.joinSession(1L);

        verify(this.sessionRepository, times(0)).save(any());
        Assertions.assertThat(joined).isFalse();
    }

    @Test
    public void testAddRestaurantToSessionWhenSessionIsThereReturnTrue() {

        when(this.restaurantService.createRestaurant(any())).thenReturn(Restaurant.builder().build());
        when(this.sessionRepository.findById(1L)).thenReturn(Optional.of(Session.builder().restaurants(new ArrayList<>()).build()));

        boolean added = this.sessionService.addRestaurantToSession(1L, RestaurantDTO.builder().build());

        verify(this.sessionRepository, times(1)).save(any());
        Assertions.assertThat(added).isTrue();
    }

    @Test
    public void testAddRestaurantToSessionWhenSessionIsNotThereReturnFalse() {

        when(this.restaurantService.createRestaurant(any())).thenReturn(Restaurant.builder().build());
        when(this.sessionRepository.findById(1L)).thenReturn(Optional.empty());

        boolean added = this.sessionService.addRestaurantToSession(1L, RestaurantDTO.builder().build());

        verify(this.sessionRepository, times(0)).save(any());
        Assertions.assertThat(added).isFalse();
    }

    @Test
    public void testEndSessionWhenSessionIsEmptyReturnNull() {
        when(this.sessionRepository.findById(1L)).thenReturn(Optional.empty());

        Session session = this.sessionService.endSession(1L);

        Assertions.assertThat(session).isNull();
    }

    @Test
    public void testEndSessionWhenSessionIsNotEmptyButNoRestaurantsReturnNull() {
        when(this.sessionRepository.findById(1L)).thenReturn(Optional.of(Session.builder().restaurants(new ArrayList<>()).build()));

       assertThrows(NORestaurantException.class, () -> this.sessionService.endSession(1L));
    }

    @Test
    public void testEndSessionWhenSessionIsNotEmptyWithRestaurantsReturnEndSession() {
        when(this.sessionRepository.findById(1L)).thenReturn(Optional.of(Session.builder().restaurants(
                Collections.singletonList(Restaurant.builder().name("wok hey").build())
        ).build()));

        Session session = this.sessionService.endSession(1L);

        verify(this.sessionRepository, times(1)).save(any());
        Assertions.assertThat(session).isNotNull();
    }

}
