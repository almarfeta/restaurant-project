package com.example.project_server.controllers;

import com.example.project_server.dtos.Response;
import com.example.project_server.dtos.ReviewRequest;
import com.example.project_server.services.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/v1/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping(path = "/add")
    public ResponseEntity<Response> addNewReview (@RequestHeader("Authorization") String bearerToken,
                                                  @RequestBody @Valid ReviewRequest review) {
        reviewService.addReview(bearerToken, review);
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(201)
                        .status(HttpStatus.CREATED)
                        .message("Server added new review successfully")
                        .build()
        );
    }

    @GetMapping("/all/restaurant/{restaurantId}")
    public ResponseEntity<Response> getReviewsForRestaurant(@PathVariable("restaurantId") Long restaurantId) {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(200)
                        .status(HttpStatus.OK)
                        .message("Server retrieved reviews with given restaurant id, ordered by up votes")
                        .data(Map.of("reviews", reviewService.getAllReviews(restaurantId)))
                        .build()
        );
    }

    @GetMapping("/all/author")
    public ResponseEntity<Response> getReviewsForAuthor(@RequestHeader("Authorization") String bearerToken) {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(200)
                        .status(HttpStatus.OK)
                        .message("Server retrieved reviews made by given author")
                        .data(Map.of("reviews", reviewService.getAllReviewsByAuthor(bearerToken)))
                        .build()
        );
    }

    @GetMapping("/all/author/and/restaurant/{restaurantId}")
    public ResponseEntity<Response> getReviewsForAuthorAndRestaurant(@RequestHeader("Authorization") String bearerToken,
                                                            @PathVariable("restaurantId") Long restaurantId) {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(200)
                        .status(HttpStatus.OK)
                        .message("Server retrieved reviews with given restaurant id, made by given author")
                        .data(Map.of("reviews", reviewService.getAllReviewsByAuthorAndRestaurant(bearerToken, restaurantId)))
                        .build()
        );
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<Response> deleteReviewById (@RequestHeader("Authorization") String bearerToken,
                                                      @PathVariable("id") Long id) {
        reviewService.deleteReviewById(bearerToken, id);
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(200)
                        .status(HttpStatus.OK)
                        .message("Review deleted successfully")
                        .build()
        );
    }
}
