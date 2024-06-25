package com.example.project_server.services;

import com.example.project_server.exceptions.BadRequestException;
import com.example.project_server.exceptions.NotFoundException;
import com.example.project_server.exceptions.UnauthorizedException;
import com.example.project_server.models.Review;
import com.example.project_server.models.UserLogin;
import com.example.project_server.models.UserProfile;
import com.example.project_server.repositories.ReviewRepository;
import com.example.project_server.repositories.UserLoginRepository;
import com.example.project_server.repositories.UserProfileRepository;
import com.example.project_server.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UpVoteService {

    private final UserLoginRepository userLoginRepository;
    private final UserProfileRepository userProfileRepository;
    private final ReviewRepository reviewRepository;
    private final JwtService jwtService;

    public void upVote(String bearerToken, Long reviewId) {
        log.info("Adding new up vote");
        String token = bearerToken.substring(7);
        String email = jwtService.extractUsername(token);
        UserProfile upVoter = userLoginRepository.findByEmail(email)
                .map(UserLogin::getUserProfile)
                .orElseThrow(() -> new NotFoundException("User not found"));
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("Review not found"));
        if (upVoter.getUpVotedReviews().contains(review)) {
            throw new BadRequestException("Already up voted");
        } else {
            upVoter.getUpVotedReviews().add(review);
            review.getUpVotedByUsers().add(upVoter);
            userProfileRepository.save(upVoter);
            reviewRepository.save(review);
        }
    }

    public List<Long> getReviewsUpVotedByUser(String bearerToken, Long restaurantId) {
        log.info("Fetching reviewIds for given restaurant and up voter");
        String token = bearerToken.substring(7);
        String email = jwtService.extractUsername(token);
        UserProfile upVoter = userLoginRepository.findByEmail(email)
                .map(UserLogin::getUserProfile)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return upVoter.getUpVotedReviews().stream()
                .filter(review -> review.getRestaurant().getId().equals(restaurantId))
                .map(Review::getId)
                .collect(Collectors.toList());
    }

    public void deleteUpVote(String bearerToken, Long reviewId) {
        log.info("Deleting up vote");
        String token = bearerToken.substring(7);
        String email = jwtService.extractUsername(token);
        UserProfile upVoter = userLoginRepository.findByEmail(email)
                .map(UserLogin::getUserProfile)
                .orElseThrow(() -> new NotFoundException("User not found"));
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("Review not found"));
        if (!upVoter.getUpVotedReviews().contains(review)) {
            throw new NotFoundException("UpVote not found");
        }
        if (upVoter.getUserLogin().getEmail().equals(email)) {
            upVoter.getUpVotedReviews().remove(review);
            review.getUpVotedByUsers().remove(upVoter);
            userProfileRepository.save(upVoter);
            reviewRepository.save(review);
        } else {
            throw new UnauthorizedException("User doesn't have permission");
        }
    }
}
