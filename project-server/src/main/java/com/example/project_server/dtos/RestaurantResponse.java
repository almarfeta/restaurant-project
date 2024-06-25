package com.example.project_server.dtos;

import com.example.project_server.models.RestaurantPicture;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestaurantResponse {

    private Long id;
    private String name;
    private String city;
    private String country;
    private String profilePicture;
    private String shortDescription;
    private LocalDate since;
    private Double rating;
    private Integer numberOfReviews;
    private List<RestaurantPicture> pictures;
}
