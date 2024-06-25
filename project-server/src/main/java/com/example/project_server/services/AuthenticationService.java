package com.example.project_server.services;

import com.example.project_server.dtos.AuthenticationRequest;
import com.example.project_server.dtos.RegisterRequest;
import com.example.project_server.consts.Role;
import com.example.project_server.consts.TokenType;
import com.example.project_server.exceptions.UnauthorizedException;
import com.example.project_server.models.Token;
import com.example.project_server.models.UserLogin;
import com.example.project_server.models.UserProfile;
import com.example.project_server.repositories.TokenRepository;
import com.example.project_server.repositories.UserLoginRepository;
import com.example.project_server.repositories.UserProfileRepository;
import com.example.project_server.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserLoginRepository userLoginRepository;
    private final UserProfileRepository userProfileRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private void saveUserToken(UserLogin savedUser, String jwtToken) {
        Token token = Token.builder()
                .userLogin(savedUser)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(UserLogin user) {
        List<Token> validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty()) {
            return;
        }
        validUserTokens.forEach(t -> {
            t.setExpired(true);
            t.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void register(RegisterRequest request) {
        log.info("Registering");
        if (userLoginRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UnauthorizedException("Email taken");
        }
        UserProfile userProfileInfo = UserProfile.builder()
                        .firstName(request.getFirstName())
                        .lastName(request.getLastName())
                        .joinDate(LocalDate.now())
                        .build();
        userProfileRepository.save(userProfileInfo);

        UserLogin user = UserLogin.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .userProfile(userProfileInfo)
                .build();
        userLoginRepository.save(user);
    }

    public String authenticate(AuthenticationRequest request) {
        log.info("Logging in");
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        UserLogin user = userLoginRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Email or password wrong"));
        String jwtToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return jwtToken;
    }
}
