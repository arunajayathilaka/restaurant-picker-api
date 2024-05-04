package com.arunadj.restaurantpickerapi.repositories;

import com.arunadj.restaurantpickerapi.entities.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
}
