package com.example.project_server.exceptions;

import com.example.project_server.dtos.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Response> handleException(BadRequestException e) {
        return new ResponseEntity<>(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(400)
                        .status(HttpStatus.BAD_REQUEST)
                        .errorMessage(e.getMessage())
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> handleException(MethodArgumentNotValidException e) {
        return new ResponseEntity<>(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(400)
                        .status(HttpStatus.BAD_REQUEST)
                        .errorMessage("Arguments not valid")
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Response> handleException(UsernameNotFoundException e) {
        return new ResponseEntity<>(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(401)
                        .status(HttpStatus.UNAUTHORIZED)
                        .errorMessage("Username or password wrong")
                        .build(),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Response> handleException(UnauthorizedException e) {
        return new ResponseEntity<>(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(401)
                        .status(HttpStatus.UNAUTHORIZED)
                        .errorMessage(e.getMessage())
                        .build(),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Response> handleException(NotFoundException e) {
        return new ResponseEntity<>(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(404)
                        .status(HttpStatus.NOT_FOUND)
                        .errorMessage(e.getMessage())
                        .build(),
                HttpStatus.NOT_FOUND
        );
    }
}
