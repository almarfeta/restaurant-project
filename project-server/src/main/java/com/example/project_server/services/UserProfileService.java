package com.example.project_server.services;

import com.example.project_server.dtos.UserInfoResponse;
import com.example.project_server.exceptions.NotFoundException;
import com.example.project_server.models.UserLogin;
import com.example.project_server.repositories.UserLoginRepository;
import com.example.project_server.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserProfileService {

    private final UserLoginRepository userLoginRepository;
    private final JwtService jwtService;

    public void checkStatus(String bearerToken) {
        log.info("Checking user token status");
        String token = bearerToken.substring(7);
        String email = jwtService.extractUsername(token);
        if (userLoginRepository.findByEmail(email).isEmpty()) {
            throw new NotFoundException("Invalid token");
        }
    }

    public UserInfoResponse getUserInfo(String bearerToken) {
        log.info("Fetching user info by token");
        String token = bearerToken.substring(7);
        String email = jwtService.extractUsername(token);
        UserLogin userLogin = userLoginRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return UserInfoResponse.builder()
                .email(userLogin.getEmail())
                .role(userLogin.getRole())
                .profileId(userLogin.getUserProfile().getId())
                .firstName(userLogin.getUserProfile().getFirstName())
                .lastName(userLogin.getUserProfile().getLastName())
                .joinDate(userLogin.getUserProfile().getJoinDate())
                .build();
    }
}
