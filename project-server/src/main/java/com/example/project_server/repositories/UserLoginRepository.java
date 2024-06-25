package com.example.project_server.repositories;

import com.example.project_server.models.UserLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserLoginRepository extends JpaRepository<UserLogin, Long> {

    Optional<UserLogin> findByEmail(String email);
}
