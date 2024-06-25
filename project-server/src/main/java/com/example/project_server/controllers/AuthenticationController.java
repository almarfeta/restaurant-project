package com.example.project_server.controllers;

import com.example.project_server.dtos.AuthenticationRequest;
import com.example.project_server.dtos.RegisterRequest;
import com.example.project_server.dtos.Response;
import com.example.project_server.services.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<Response> register(@RequestBody @Valid RegisterRequest request) {
        service.register(request);
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(201)
                        .status(HttpStatus.CREATED)
                        .message("Registered successfully")
                        .build()
        );
    }

    @PostMapping("/authenticate")
    public ResponseEntity<Response> authenticate(@RequestBody @Valid AuthenticationRequest request) {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(200)
                        .status(HttpStatus.OK)
                        .message("Login successfully")
                        .data(Map.of("token", service.authenticate(request)))
                        .build()
        );
    }
}
