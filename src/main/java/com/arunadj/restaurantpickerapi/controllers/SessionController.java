package com.arunadj.restaurantpickerapi.controllers;

import com.arunadj.restaurantpickerapi.dtos.DataResponseDTO;
import com.arunadj.restaurantpickerapi.dtos.RestaurantDTO;
import com.arunadj.restaurantpickerapi.dtos.SessionDTO;
import com.arunadj.restaurantpickerapi.dtos.UserDTO;
import com.arunadj.restaurantpickerapi.entities.Restaurant;
import com.arunadj.restaurantpickerapi.entities.Session;
import com.arunadj.restaurantpickerapi.services.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    @Autowired
    private SessionService sessionService;

    @CrossOrigin
    @PostMapping
    public ResponseEntity<Session> createSession(@RequestBody SessionDTO sessionDTO) {
        Session createdSession = sessionService.createSession(sessionDTO);
        return ResponseEntity.ok(createdSession);
    }

    @GetMapping("/{sessionId}")
    public ResponseEntity<Session> getSessionById(@PathVariable Long sessionId) {
        Session session = sessionService.getSessionById(sessionId);
        if (session == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(session);
    }

    @GetMapping("/{sessionId}/isEnded")
    public ResponseEntity<DataResponseDTO<Boolean>> isSessionEndedById(@PathVariable Long sessionId) {
        Session session = sessionService.getSessionById(sessionId);
        if (session == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(DataResponseDTO.<Boolean>builder().status("SUCCESS").message("").data(session.getSelectedRestaurant() == null).build());
    }

    @GetMapping
    public ResponseEntity<List<Session>> getAllSessions() {
        List<Session> sessions = sessionService.getAllSessions();
        return ResponseEntity.ok(sessions);
    }

    @PutMapping("/{sessionId}")
    public ResponseEntity<Void> updateSession(@PathVariable Long sessionId, @RequestBody SessionDTO sessionDTO) {
        boolean updated = sessionService.updateSession(sessionId, sessionDTO);
        if (!updated) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{sessionId}")
    public ResponseEntity<Void> deleteSession(@PathVariable Long sessionId) {
        sessionService.deleteSession(sessionId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{sessionId}/join")
    public ResponseEntity<Void> joinSession(@PathVariable Long sessionId) {
        boolean joined = sessionService.joinSession(sessionId);
        if (!joined) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{sessionId}/restaurants")
    public ResponseEntity<List<Restaurant>> getRestaurants(@PathVariable Long sessionId) {
        List<Restaurant> restaurants = sessionService.getRestaurants(sessionId);

        return ResponseEntity.ok(restaurants);
    }

    @PostMapping("/{sessionId}/addRestaurant")
    public ResponseEntity<Void> addRestaurant(@PathVariable Long sessionId, @RequestBody RestaurantDTO restaurantDTO) {
        boolean added = sessionService.addRestaurantToSession(sessionId, restaurantDTO);
        if (!added) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{sessionId}/end")
    public ResponseEntity<Session> endSession(@PathVariable Long sessionId) {
        Session endedSession = sessionService.endSession(sessionId);
        if (endedSession == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(endedSession);
    }

}