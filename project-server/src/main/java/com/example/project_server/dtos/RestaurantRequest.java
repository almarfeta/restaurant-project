package com.example.project_server.dtos;

import com.example.project_server.consts.OnCreate;
import com.example.project_server.consts.OnUpdate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantRequest {

    @Null(groups = OnCreate.class)
    @NotNull(groups = OnUpdate.class)
    private Long id;

    @NotBlank(groups = OnCreate.class)
    private String name;

    @NotBlank(groups = OnCreate.class)
    @Null(groups = OnUpdate.class)
    private String city;

    @NotBlank(groups = OnCreate.class)
    @Null(groups = OnUpdate.class)
    private String country;

    @NotBlank(groups = OnCreate.class)
    private String profilePicture;

    private String shortDescription;
}
