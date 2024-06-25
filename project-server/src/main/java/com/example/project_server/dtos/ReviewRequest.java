package com.example.project_server.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRequest {

    @NotNull
    private Long restaurantId;

    @NotBlank
    private String title;

    @NotBlank
    private String note;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer rating;
}
