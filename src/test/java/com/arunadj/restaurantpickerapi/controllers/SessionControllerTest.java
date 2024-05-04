package com.arunadj.restaurantpickerapi.controllers;

import com.arunadj.restaurantpickerapi.dtos.DataResponseDTO;
import com.arunadj.restaurantpickerapi.dtos.RestaurantDTO;
import com.arunadj.restaurantpickerapi.dtos.SessionDTO;
import com.arunadj.restaurantpickerapi.entities.Restaurant;
import com.arunadj.restaurantpickerapi.entities.Session;
import com.arunadj.restaurantpickerapi.services.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class SessionControllerTest {

    @Mock
    private SessionService sessionService;

    @InjectMocks
    private SessionController sessionController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateSession() {
        SessionDTO sessionDTO = new SessionDTO();
        Session createdSession = new Session();
        when(sessionService.createSession(any())).thenReturn(createdSession);

        ResponseEntity<Session> response = sessionController.createSession(sessionDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(createdSession, response.getBody());
    }

    @Test
    public void testGetSessionById() {
        Long sessionId = 1L;
        Session session = new Session();
        when(sessionService.getSessionById(sessionId)).thenReturn(session);

        ResponseEntity<Session> response = sessionController.getSessionById(sessionId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(session, response.getBody());
    }

    @Test
    public void testIsSessionEndedByIdWhenSessionExistsReturnOk() {
        Long sessionId = 1L;
        Session session = new Session();
        session.setSelectedRestaurant(null);
        when(sessionService.getSessionById(sessionId)).thenReturn(session);

        ResponseEntity<DataResponseDTO<Boolean>> response = sessionController.isSessionEndedById(sessionId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().getData());
    }

    @Test
    public void testIsSessionEndedByIdWhenSessionNotExistsReturn404() {
        Long sessionId = 1L;
        when(sessionService.getSessionById(sessionId)).thenReturn(null);

        ResponseEntity<DataResponseDTO<Boolean>> response = sessionController.isSessionEndedById(sessionId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetAllSessions() {
        List<Session> sessions = Collections.singletonList(new Session());
        when(sessionService.getAllSessions()).thenReturn(sessions);

        ResponseEntity<List<Session>> response = sessionController.getAllSessions();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sessions, response.getBody());
    }

    @Test
    public void testUpdateSession() {
        Long sessionId = 1L;
        SessionDTO sessionDTO = new SessionDTO();
        when(sessionService.updateSession(sessionId, sessionDTO)).thenReturn(true);

        ResponseEntity<Void> response = sessionController.updateSession(sessionId, sessionDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteSession() {
        Long sessionId = 1L;

        ResponseEntity<Void> response = sessionController.deleteSession(sessionId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testJoinSessionWhenJoinedReturnOk() {
        Long sessionId = 1L;
        when(sessionService.joinSession(sessionId)).thenReturn(true);

        ResponseEntity<Void> response = sessionController.joinSession(sessionId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testJoinSessionWhenNotJoinedReturn404() {
        Long sessionId = 1L;
        when(sessionService.joinSession(sessionId)).thenReturn(false);

        ResponseEntity<Void> response = sessionController.joinSession(sessionId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetRestaurants() {
        Long sessionId = 1L;
        List<Restaurant> restaurants = Collections.singletonList(new Restaurant());
        when(sessionService.getRestaurants(sessionId)).thenReturn(restaurants);

        ResponseEntity<List<Restaurant>> response = sessionController.getRestaurants(sessionId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(restaurants, response.getBody());
    }

    @Test
    public void testAddRestaurant() {
        Long sessionId = 1L;
        RestaurantDTO restaurantDTO = new RestaurantDTO();
        when(sessionService.addRestaurantToSession(sessionId, restaurantDTO)).thenReturn(true);

        ResponseEntity<Void> response = sessionController.addRestaurant(sessionId, restaurantDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testEndSessionWhenSessionExists() {
        Long sessionId = 1L;
        Session endedSession = new Session();
        when(sessionService.endSession(sessionId)).thenReturn(endedSession);

        ResponseEntity<Session> response = sessionController.endSession(sessionId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(endedSession, response.getBody());
    }

    @Test
    public void testEndSessionWhenSessionNotExists() {
        Long sessionId = 1L;
        when(sessionService.endSession(sessionId)).thenReturn(null);

        ResponseEntity<Session> response = sessionController.endSession(sessionId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

}
