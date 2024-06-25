package com.example.project_server.repositories;

import com.example.project_server.models.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    Page<Restaurant> findAllByNameContainingIgnoreCase(String name, PageRequest pageRequest);
}
