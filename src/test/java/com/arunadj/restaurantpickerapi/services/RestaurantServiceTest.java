package com.arunadj.restaurantpickerapi.services;

import com.arunadj.restaurantpickerapi.entities.Restaurant;
import com.arunadj.restaurantpickerapi.repositories.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class RestaurantServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private RestaurantService restaurantService;

    public RestaurantServiceTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateRestaurant() {
        Restaurant restaurant = new Restaurant();
        when(restaurantRepository.save(restaurant)).thenReturn(restaurant);

        Restaurant createdRestaurant = restaurantService.createRestaurant(restaurant);

        assertEquals(restaurant, createdRestaurant);
        verify(restaurantRepository, times(1)).save(restaurant);
    }

    @Test
    public void testGetAllRestaurants() {
        List<Restaurant> restaurants = new ArrayList<>();
        restaurants.add(new Restaurant());
        restaurants.add(new Restaurant());
        when(restaurantRepository.findAll()).thenReturn(restaurants);

        List<Restaurant> retrievedRestaurants = restaurantService.getAllRestaurants();

        assertEquals(restaurants.size(), retrievedRestaurants.size());
        assertEquals(restaurants, retrievedRestaurants);
        verify(restaurantRepository, times(1)).findAll();
    }
}
