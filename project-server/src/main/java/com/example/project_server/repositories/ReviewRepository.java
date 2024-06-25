package com.example.project_server.repositories;

import com.example.project_server.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("""
    SELECT r FROM review r
    WHERE r.restaurant.id = :restaurantId
    ORDER BY size(r.upVotedByUsers) DESC
    """)
    List<Review> findAllByRestaurantIdOrderByNumberOfUpVotesDesc(Long restaurantId);

    List<Review> findAllByAuthorId(Long authorId);

    List<Review> findAllByRestaurantIdAndAuthorId(Long restaurantId, Long authorId);
}
