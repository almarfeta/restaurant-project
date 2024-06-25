package com.example.project_server.controllers;

import com.example.project_server.dtos.Response;
import com.example.project_server.dtos.RestaurantPageResponse;
import com.example.project_server.dtos.RestaurantRequest;
import com.example.project_server.consts.OnCreate;
import com.example.project_server.consts.OnUpdate;
import com.example.project_server.services.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/v1/restaurant")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping(path = "/add")
    public ResponseEntity<Response> addNewRestaurant(@RequestHeader("Authorization") String bearerToken,
                                                     @RequestBody @Validated(OnCreate.class) RestaurantRequest restaurant) {
        restaurantService.addRestaurant(bearerToken, restaurant);
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(201)
                        .status(HttpStatus.CREATED)
                        .message("Server added restaurant successfully")
                        .build()
        );
    }

    @PostMapping("/add/picture")
    public ResponseEntity<Response> addRestaurantPicture(@RequestHeader("Authorization") String bearerToken,
                                                       @RequestParam("restaurantId") Long id,
                                                       @RequestParam("picture") String picture) {
        restaurantService.addPicture(bearerToken, id, picture);
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(201)
                        .status(HttpStatus.CREATED)
                        .message("Server added restaurant picture successfully")
                        .build()
        );
    }

    @GetMapping(path = "/all")
    public ResponseEntity<Response> getRestaurants(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size,
                                                   @RequestParam(defaultValue = "name") String sortField,
                                                   @RequestParam(defaultValue = "asc") String sortOrder) {
        RestaurantPageResponse mappedPage = restaurantService.getRestaurants(
                PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortOrder), sortField))
        );
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(200)
                        .status(HttpStatus.OK)
                        .message("Server retrieved restaurants")
                        .data(Map.of("page", mappedPage))
                        .build()
        );
    }

    @GetMapping(path = "/all/name/{name}")
    public ResponseEntity<Response> getRestaurantsByName(@PathVariable("name") String name,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size,
                                                         @RequestParam(defaultValue = "name") String sortField,
                                                         @RequestParam(defaultValue = "asc") String sortOrder) {
        RestaurantPageResponse mappedPage = restaurantService.getRestaurantsByName(name,
                PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortOrder), sortField))
        );
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(200)
                        .status(HttpStatus.OK)
                        .message("Server retrieved restaurants with given name")
                        .data(Map.of("page", mappedPage))
                        .build()
        );
    }

    @GetMapping(path = "/by/name/{id}")
    public ResponseEntity<Response> getRestaurantInfo(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(200)
                        .status(HttpStatus.OK)
                        .message("Server retrieved restaurant profile")
                        .data(Map.of("restaurantProfile", restaurantService.getRestaurantById(id)))
                        .build()
        );
    }

    @GetMapping(path = "/managed")
    public ResponseEntity<Response> getRestaurantsByManager(@RequestHeader("Authorization") String bearerToken) {
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(200)
                        .status(HttpStatus.OK)
                        .message("Server retrieved restaurants managed by the user")
                        .data(Map.of("restaurants", restaurantService.getRestaurantsByManager(bearerToken)))
                        .build()
        );
    }

    @PutMapping("/update")
    public ResponseEntity<Response> updateRestaurantInfo(@RequestHeader("Authorization") String bearerToken,
                                @RequestBody @Validated(OnUpdate.class) RestaurantRequest restaurantRequest) {
        restaurantService.updateRestaurantInfo(bearerToken, restaurantRequest);
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(200)
                        .status(HttpStatus.OK)
                        .message("Server updated restaurant info")
                        .build()
        );
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<Response> deleteRestaurantById(@RequestHeader("Authorization") String bearerToken,
                                                         @PathVariable("id") Long id) {
        restaurantService.deleteRestaurantById(bearerToken, id);
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(200)
                        .status(HttpStatus.OK)
                        .message("Restaurant deleted successfully")
                        .build()
        );
    }

    @DeleteMapping(path = "/delete/picture")
    public ResponseEntity<Response> deleteRestaurantPictureById(@RequestHeader("Authorization") String bearerToken,
                                                         @RequestParam("restaurantId") Long restaurantId,
                                                         @RequestParam("pictureId") Long pictureId) {
        restaurantService.deleteRestaurantPictureById(bearerToken, restaurantId, pictureId);
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(200)
                        .status(HttpStatus.OK)
                        .message("Restaurant picture deleted successfully")
                        .build()
        );
    }
}
