package com.example.project_server.controllers;

import com.example.project_server.dtos.Response;
import com.example.project_server.services.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserProfileService userProfileService;

    @GetMapping("/check")
    public ResponseEntity<Response> checkStatus(@RequestHeader(value = "Authorization") String bearerToken) {
        userProfileService.checkStatus(bearerToken);
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(200)
                        .status(HttpStatus.OK)
                        .message("Valid")
                        .build()
        );
    }

    @GetMapping("/data")
    public ResponseEntity<Response> getUserInfo(@RequestHeader("Authorization") String bearerToken) {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(200)
                        .status(HttpStatus.OK)
                        .message("Server retrieved user info")
                        .data(Map.of("userInfo", userProfileService.getUserInfo(bearerToken)))
                        .build()
        );
    }

//    @PreAuthorize("hasAuthority('admin:read')")
//    @GetMapping(path = "/data/{id}")
//    public ResponseEntity<Response> getUserInfoById(@PathVariable("id") Long id) {
//        return ResponseEntity.ok(
//                Response.builder()
//                        .timeStamp(LocalDateTime.now())
//                        .statusCode(200)
//                        .status(HttpStatus.OK)
//                        .message("Server retrieved user info")
//                        .data(Map.of("userInfo", userProfileService.getUserById(id)))
//                        .build()
//        );
//    }
}
