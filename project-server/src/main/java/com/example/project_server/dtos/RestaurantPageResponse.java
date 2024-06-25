package com.example.project_server.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestaurantPageResponse {

    private List<RestaurantResponse> restaurants;
    private long totalElements;
}
