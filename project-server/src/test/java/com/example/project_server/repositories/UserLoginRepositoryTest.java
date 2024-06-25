package com.example.project_server.repositories;

import com.example.project_server.consts.Role;
import com.example.project_server.models.UserLogin;
import com.example.project_server.models.UserProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
class UserLoginRepositoryTest {

    @Autowired
    private UserLoginRepository userLoginRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @BeforeEach
    void beforeEach() {
        UserProfile userProfile = UserProfile.builder()
                .firstName("John")
                .lastName("Doe")
                .joinDate(LocalDate.of(2024, 6, 5))
                .build();
        UserLogin userLogin = UserLogin.builder()
                .email("test@email.com")
                .password("someEncodedPassword")
                .role(Role.USER)
                .userProfile(userProfile)
                .build();
        this.userProfileRepository.save(userProfile);
        this.userLoginRepository.save(userLogin);
    }

    @Test
    @DisplayName("Should find a user by Email")
    void findByEmail() {
        Optional<UserLogin> user = this.userLoginRepository.findByEmail("test@email.com");
        assertTrue(user.isPresent());
        assertEquals("test@email.com", user.get().getEmail());
        assertEquals("someEncodedPassword", user.get().getPassword());
        assertEquals("John", user.get().getUserProfile().getFirstName());
        assertEquals("Doe", user.get().getUserProfile().getLastName());
    }
}