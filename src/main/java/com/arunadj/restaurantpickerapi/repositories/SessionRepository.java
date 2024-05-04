package com.arunadj.restaurantpickerapi.repositories;

import com.arunadj.restaurantpickerapi.entities.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
}
