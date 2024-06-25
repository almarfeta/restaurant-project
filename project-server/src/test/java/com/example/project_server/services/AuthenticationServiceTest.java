package com.example.project_server.services;

import com.example.project_server.dtos.AuthenticationRequest;
import com.example.project_server.dtos.RegisterRequest;
import com.example.project_server.exceptions.UnauthorizedException;
import com.example.project_server.models.UserLogin;
import com.example.project_server.models.UserProfile;
import com.example.project_server.repositories.TokenRepository;
import com.example.project_server.repositories.UserLoginRepository;
import com.example.project_server.repositories.UserProfileRepository;
import com.example.project_server.security.JwtService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserLoginRepository userLoginRepository;

    @Mock
    private UserProfileRepository userProfileRepository;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    @DisplayName("Should register a user successfully")
    void registerSuccessful() {
        RegisterRequest request = new RegisterRequest(
                "John",
                "Doe",
                "test@email.com",
                "Password1234"
        );
        when(this.userLoginRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.empty());
        when(this.passwordEncoder.encode(request.getPassword()))
                .thenReturn("encodedPassword");

        this.authenticationService.register(request);

        ArgumentCaptor<UserProfile> userProfileCaptor = ArgumentCaptor.forClass(UserProfile.class);
        verify(this.userProfileRepository).save(userProfileCaptor.capture());
        assertEquals("John", userProfileCaptor.getValue().getFirstName());

        ArgumentCaptor<UserLogin> userLoginCaptor = ArgumentCaptor.forClass(UserLogin.class);
        verify(this.userLoginRepository).save(userLoginCaptor.capture());
        assertEquals("test@email.com", userLoginCaptor.getValue().getEmail());
        assertEquals("encodedPassword", userLoginCaptor.getValue().getPassword());
    }

    @Test
    @DisplayName("Should throw UnauthorizedException when registering")
    void registerEmailTaken() {
        RegisterRequest request = new RegisterRequest(
                "John",
                "Doe",
                "test@email.com",
                "Password1234"
        );
        when(this.userLoginRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.of(new UserLogin()));

        assertThrows(UnauthorizedException.class,
                () -> this.authenticationService.register(request));
        verify(this.userProfileRepository, never()).save(any(UserProfile.class));
        verify(this.userLoginRepository, never()).save(any(UserLogin.class));
    }

    @Test
    @DisplayName("Should login a user successfully")
    void authenticateSuccessful() {
        AuthenticationRequest request = new AuthenticationRequest(
                "test@email.com",
                "Password1234"
        );
        UserLogin userLogin = UserLogin.builder()
                .email("test@email.com")
                .password("encodedPassword")
                .build();
        when(this.userLoginRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.of(userLogin));
        when(this.jwtService.generateToken(userLogin))
                .thenReturn("jwtToken");

        String returnedJwt = this.authenticationService.authenticate(request);
        assertEquals("jwtToken", returnedJwt);
    }

    @Test
    @DisplayName("Should throw UnauthorizedException")
    void authenticateFail() {
        AuthenticationRequest request = new AuthenticationRequest(
                "test@email.com",
                "Password1234"
        );
        when(this.userLoginRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.empty());

        assertThrows(UnauthorizedException.class,
                () -> this.authenticationService.authenticate(request));
        verify(this.jwtService, never()).generateToken(new UserLogin());
    }
}