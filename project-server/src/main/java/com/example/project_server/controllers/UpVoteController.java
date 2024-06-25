package com.example.project_server.controllers;

import com.example.project_server.dtos.Response;
import com.example.project_server.services.UpVoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/v1/upvote")
@RequiredArgsConstructor
public class UpVoteController {

    private final UpVoteService upVoteService;

    @PostMapping("/add/{id}")
    public ResponseEntity<Response> upVote(@RequestHeader("Authorization") String bearerToken,
                                           @PathVariable("id") Long reviewId) {
        upVoteService.upVote(bearerToken, reviewId);
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(201)
                        .status(HttpStatus.CREATED)
                        .message("Up voted successfully")
                        .build()
        );
    }

    @GetMapping("/all/upvoted/restaurant/{restaurantId}")
    public ResponseEntity<Response> getReviewsUpVotedByUser(@RequestHeader("Authorization") String bearerToken,
                                                            @PathVariable("restaurantId") Long restaurantId) {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(200)
                        .status(HttpStatus.OK)
                        .message("Server retrieved reviewIds up voted by the user on given restaurant")
                        .data(Map.of("reviewIds", upVoteService.getReviewsUpVotedByUser(bearerToken, restaurantId)))
                        .build()
        );
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<Response> deleteUpVote(@RequestHeader("Authorization") String bearerToken,
                                                 @PathVariable("id") Long reviewId) {
        upVoteService.deleteUpVote(bearerToken, reviewId);
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(200)
                        .status(HttpStatus.OK)
                        .message("Up vote deleted successfully")
                        .build()
        );
    }
}
