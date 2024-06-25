package com.example.project_server.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {

    private LocalDateTime timeStamp;
    private int statusCode;
    private HttpStatus status;
    private String message;
    private String errorMessage;
    private Map<?, ?> data;
}
