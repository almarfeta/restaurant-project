package com.example.project_server.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReviewResponse {

    private Long id;
    private String title;
    private Integer rating;
    private String note;
    private LocalDate date;
    private UserInfoResponse author;
    private Integer numberOfUpVotes;
    private Long restaurantId;
    private String restaurantName;
}
