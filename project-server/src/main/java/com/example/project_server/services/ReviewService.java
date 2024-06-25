package com.example.project_server.services;

import com.example.project_server.dtos.ReviewRequest;
import com.example.project_server.dtos.ReviewResponse;
import com.example.project_server.dtos.UserInfoResponse;
import com.example.project_server.exceptions.NotFoundException;
import com.example.project_server.exceptions.UnauthorizedException;
import com.example.project_server.models.Restaurant;
import com.example.project_server.models.Review;
import com.example.project_server.models.UserLogin;
import com.example.project_server.models.UserProfile;
import com.example.project_server.repositories.RestaurantRepository;
import com.example.project_server.repositories.ReviewRepository;
import com.example.project_server.repositories.UserLoginRepository;
import com.example.project_server.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserLoginRepository userLoginRepository;
    private final JwtService jwtService;

    public void addReview(String bearerToken, ReviewRequest reviewRequest) {
        log.info("Adding new review");
        String token = bearerToken.substring(7);
        String email = jwtService.extractUsername(token);
        UserProfile author = userLoginRepository.findByEmail(email)
                .map(UserLogin::getUserProfile)
                .orElseThrow(() -> new NotFoundException("User not found"));
        Restaurant restaurant = restaurantRepository.findById(reviewRequest.getRestaurantId())
                .orElseThrow(() -> new NotFoundException("Restaurant not found"));
        Review review = Review.builder()
                .title(reviewRequest.getTitle())
                .rating(reviewRequest.getRating())
                .note(reviewRequest.getNote())
                .date(LocalDate.now())
                .author(author)
                .restaurant(restaurant)
                .build();
        reviewRepository.save(review);
    }

    public List<ReviewResponse> getAllReviews(Long restaurantId) {
        log.info("Fetching reviews for given restaurant");
        return reviewRepository.findAllByRestaurantIdOrderByNumberOfUpVotesDesc(restaurantId).stream()
                .map(review -> ReviewResponse.builder()
                        .id(review.getId())
                        .title(review.getTitle())
                        .note(review.getNote())
                        .date(review.getDate())
                        .rating(review.getRating())
                        .numberOfUpVotes(review.getNumberOfUpVotes())
                        .author(UserInfoResponse.builder()
                                .profileId(review.getAuthor().getId())
                                .firstName(review.getAuthor().getFirstName())
                                .lastName(review.getAuthor().getLastName())
                                .joinDate(review.getAuthor().getJoinDate())
                                .build())
                        .build())
                .collect(Collectors.toList());
    }

    public List<ReviewResponse> getAllReviewsByAuthor(String bearerToken) {
        log.info("Fetching reviews for given author");
        String token = bearerToken.substring(7);
        String email = jwtService.extractUsername(token);
        UserProfile author = userLoginRepository.findByEmail(email)
                .map(UserLogin::getUserProfile)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return reviewRepository.findAllByAuthorId(author.getId()).stream()
                .map(review -> ReviewResponse.builder()
                        .id(review.getId())
                        .title(review.getTitle())
                        .note(review.getNote())
                        .date(review.getDate())
                        .rating(review.getRating())
                        .numberOfUpVotes(review.getNumberOfUpVotes())
                        .restaurantId(review.getRestaurant().getId())
                        .restaurantName(review.getRestaurant().getName())
                        .build())
                .collect(Collectors.toList());
    }

    public List<ReviewResponse> getAllReviewsByAuthorAndRestaurant(String bearerToken, Long restaurantId) {
        log.info("Fetching reviews for given restaurant and author");
        String token = bearerToken.substring(7);
        String email = jwtService.extractUsername(token);
        UserProfile author = userLoginRepository.findByEmail(email)
                .map(UserLogin::getUserProfile)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return reviewRepository.findAllByRestaurantIdAndAuthorId(restaurantId, author.getId()).stream()
                .map(review -> ReviewResponse.builder()
                        .id(review.getId())
                        .title(review.getTitle())
                        .note(review.getNote())
                        .date(review.getDate())
                        .rating(review.getRating())
                        .numberOfUpVotes(review.getNumberOfUpVotes())
                        .build())
                .collect(Collectors.toList());
    }

    public void deleteReviewById(String bearerToken, Long id) {
        log.info("Deleting review with given id");
        String token = bearerToken.substring(7);
        String email = jwtService.extractUsername(token);
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Review not found"));
        if (review.getAuthor().getUserLogin().getEmail().equals(email)) {
            reviewRepository.deleteById(id);
        } else {
            throw new UnauthorizedException("User doesn't have permission");
        }
    }
}
