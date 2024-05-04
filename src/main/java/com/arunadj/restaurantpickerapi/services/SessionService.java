package com.arunadj.restaurantpickerapi.services;

import com.arunadj.restaurantpickerapi.dtos.RestaurantDTO;
import com.arunadj.restaurantpickerapi.dtos.SessionDTO;
import com.arunadj.restaurantpickerapi.dtos.UserDTO;
import com.arunadj.restaurantpickerapi.entities.Restaurant;
import com.arunadj.restaurantpickerapi.entities.Session;
import com.arunadj.restaurantpickerapi.entities.User;
import com.arunadj.restaurantpickerapi.exceptions.NORestaurantException;
import com.arunadj.restaurantpickerapi.repositories.SessionRepository;
import com.arunadj.restaurantpickerapi.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.rmi.server.UID;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@Slf4j
public class SessionService {
    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestaurantService restaurantService;

    public Session createSession(SessionDTO session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return sessionRepository.save(Session.builder().name(session.getName()).sessionIdentifier(UUID.randomUUID()).createdBy(authentication.getName()).created(LocalDateTime.now()).build());
    }

    public Session getSessionById(Long sessionId) {
        return sessionRepository.findById(sessionId).orElse(null);
    }

    public List<Restaurant> getRestaurants(Long sessionId) {
        Optional<Session> session = sessionRepository.findById(sessionId);
        if (session.isPresent()) {
            return session.get().getRestaurants();
        }
        return new ArrayList<>();
    }

    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }

    public boolean updateSession(Long sessionId, SessionDTO sessionDTO) {
        if (!sessionRepository.existsById(sessionId)) {
            log.info("already exists");
            return false;
        }
        Session session = Session.builder().name(sessionDTO.getName()).id(sessionId).build();
        sessionRepository.save(session);
        return true;
    }

    public void deleteSession(Long sessionId) {
        sessionRepository.deleteById(sessionId);
    }

    public boolean joinSession(Long sessionId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Optional<User> user = userRepository.findByUsername(authentication.getName());
        Session session = sessionRepository.findById(sessionId).orElse(null);
        if (session == null) {
            return false;
        }
        if (user.isPresent()) {
            session.getUsers().add(user.get());
            sessionRepository.save(session);
        }
        return true;
    }

    public boolean addRestaurantToSession(Long sessionId, RestaurantDTO restaurantDTO) {
        Restaurant restaurant = restaurantService.createRestaurant(Restaurant.builder()
                .name(restaurantDTO.getName())
                .lat(restaurantDTO.getLat())
                        .lng(restaurantDTO.getLng())
                        .address(restaurantDTO.getAddress())
                .build());
        Session session = sessionRepository.findById(sessionId).orElse(null);
        if (session == null) {
            return false;
        }

        session.getRestaurants().add(restaurant);
        sessionRepository.save(session);
        return true;
    }

    public Session endSession(Long sessionId) {
        Session session = sessionRepository.findById(sessionId).orElse(null);
        if (session == null) {
            return null;
        }

        Restaurant selectedRestaurant = selectRandomRestaurant(session.getRestaurants());
        if (selectedRestaurant == null) {
            throw new NORestaurantException("No Restaurants in session");
        }
        session.setSelectedRestaurant(selectedRestaurant.getName());
        sessionRepository.save(session);
        return session;
    }

    private Restaurant selectRandomRestaurant(List<Restaurant> restaurants) {
        if (restaurants == null || restaurants.isEmpty()) {
            return null;
        }
        Random random = new Random();
        int index = random.nextInt(restaurants.size());
        return restaurants.get(index);
    }

}
