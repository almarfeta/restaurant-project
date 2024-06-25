package com.example.project_server.repositories;

import com.example.project_server.models.RestaurantPicture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantPictureRepository extends JpaRepository<RestaurantPicture, Long> {
}
