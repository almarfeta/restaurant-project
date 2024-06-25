package com.example.project_server.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank
    @Pattern(regexp = "[a-zA-Z \\-]+")
    private String firstName;

    @NotBlank
    @Pattern(regexp = "[a-zA-Z \\-]+")
    private String lastName;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;
}
