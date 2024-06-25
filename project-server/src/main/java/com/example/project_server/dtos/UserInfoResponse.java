package com.example.project_server.dtos;

import com.example.project_server.consts.Role;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserInfoResponse {

    private String email;
    private Role role;
    private Long profileId;
    private String firstName;
    private String lastName;
    private LocalDate joinDate;
}
