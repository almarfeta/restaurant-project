package com.example.project_server.services;

import com.example.project_server.dtos.RestaurantPageResponse;
import com.example.project_server.dtos.RestaurantRequest;
import com.example.project_server.dtos.RestaurantResponse;
import com.example.project_server.exceptions.NotFoundException;
import com.example.project_server.exceptions.UnauthorizedException;
import com.example.project_server.models.Restaurant;
import com.example.project_server.models.RestaurantPicture;
import com.example.project_server.models.UserLogin;
import com.example.project_server.models.UserProfile;
import com.example.project_server.repositories.RestaurantPictureRepository;
import com.example.project_server.repositories.RestaurantRepository;
import com.example.project_server.repositories.UserLoginRepository;
import com.example.project_server.security.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantPictureRepository restaurantPictureRepository;
    private final UserLoginRepository userLoginRepository;
    private final JwtService jwtService;

    public void addRestaurant(String bearerToken, RestaurantRequest restaurantRequest) {
        log.info("Adding new restaurant");
        String token = bearerToken.substring(7);
        String email = jwtService.extractUsername(token);
        UserProfile manager = userLoginRepository.findByEmail(email)
                .map(UserLogin::getUserProfile)
                .orElseThrow(() -> new NotFoundException("User not found"));
        Restaurant restaurant = Restaurant.builder()
                .name(restaurantRequest.getName())
                .city(restaurantRequest.getCity())
                .country(restaurantRequest.getCountry())
                .profilePicture(restaurantRequest.getProfilePicture())
                .shortDescription(restaurantRequest.getShortDescription())
                .since(LocalDate.now())
                .manager(manager)
                .build();
        restaurantRepository.save(restaurant);
    }

    public void addPicture(String bearerToken, Long id, String picture) {
        log.info("Adding restaurant picture");
        String token = bearerToken.substring(7);
        String email = jwtService.extractUsername(token);
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Restaurant not found"));
        if (restaurant.getManager().getUserLogin().getEmail().equals(email)) {
            RestaurantPicture restaurantPicture = RestaurantPicture.builder()
                    .picture(picture)
                    .restaurant(restaurant)
                    .build();
            restaurantPictureRepository.save(restaurantPicture);
        } else {
            throw new UnauthorizedException("User doesn't have permission");
        }
    }

    public RestaurantPageResponse getRestaurants(PageRequest pageRequest) {
        log.info("Fetching all restaurants");
        Page<Restaurant> restaurantPage = this.restaurantRepository.findAll(pageRequest);
        List<RestaurantResponse> restaurants = restaurantPage.getContent().stream()
                .map(restaurant -> RestaurantResponse.builder()
                        .id(restaurant.getId())
                        .name(restaurant.getName())
                        .city(restaurant.getCity())
                        .country(restaurant.getCountry())
                        .profilePicture(restaurant.getProfilePicture())
                        .shortDescription(restaurant.getShortDescription())
                        .rating(restaurant.getRating())
                        .numberOfReviews(restaurant.getNumberOfReviews())
                        .build())
                .toList();
        return RestaurantPageResponse.builder()
                .restaurants(restaurants)
                .totalElements(restaurantPage.getTotalElements())
                .build();
    }

    public RestaurantPageResponse getRestaurantsByName(String name, PageRequest pageRequest) {
        log.info("Fetching restaurants with given name");
        Page<Restaurant> restaurantPage = this.restaurantRepository.findAllByNameContainingIgnoreCase(name, pageRequest);
        List<RestaurantResponse> restaurants = restaurantPage.getContent().stream()
                .map(restaurant -> RestaurantResponse.builder()
                        .id(restaurant.getId())
                        .name(restaurant.getName())
                        .city(restaurant.getCity())
                        .country(restaurant.getCountry())
                        .profilePicture(restaurant.getProfilePicture())
                        .shortDescription(restaurant.getShortDescription())
                        .rating(restaurant.getRating())
                        .numberOfReviews(restaurant.getNumberOfReviews())
                        .build())
                .toList();
        return RestaurantPageResponse.builder()
                .restaurants(restaurants)
                .totalElements(restaurantPage.getTotalElements())
                .build();
    }

    public RestaurantResponse getRestaurantById(Long id) {
        log.info("Fetching restaurant profile by id");
        return restaurantRepository.findById(id)
                .map(restaurant -> RestaurantResponse.builder()
                        .name(restaurant.getName())
                        .city(restaurant.getCity())
                        .country(restaurant.getCountry())
                        .profilePicture(restaurant.getProfilePicture())
                        .shortDescription(restaurant.getShortDescription())
                        .since(restaurant.getSince())
                        .rating(restaurant.getRating())
                        .numberOfReviews(restaurant.getNumberOfReviews())
                        .pictures(restaurant.getPictures())
                        .build())
                .orElseThrow(() -> new NotFoundException("Restaurant not found"));
    }

    public List<RestaurantResponse> getRestaurantsByManager(String bearerToken) {
        String token = bearerToken.substring(7);
        String email = jwtService.extractUsername(token);
        UserProfile userProfile = userLoginRepository.findByEmail(email)
                .map(UserLogin::getUserProfile)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return userProfile.getRestaurantsManaged().stream()
                .map(restaurant -> RestaurantResponse.builder()
                        .id(restaurant.getId())
                        .name(restaurant.getName())
                        .city(restaurant.getCity())
                        .country(restaurant.getCountry())
                        .profilePicture(restaurant.getProfilePicture())
                        .shortDescription(restaurant.getShortDescription())
                        .rating(restaurant.getRating())
                        .numberOfReviews(restaurant.getNumberOfReviews())
                        .pictures(restaurant.getPictures())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateRestaurantInfo(String bearerToken, RestaurantRequest restaurantRequest) {
        log.info("Updating restaurant");
        String token = bearerToken.substring(7);
        String email = jwtService.extractUsername(token);
        Restaurant restaurant = restaurantRepository.findById(restaurantRequest.getId())
                .orElseThrow(() -> new NotFoundException("Restaurant not found"));
        if (restaurant.getManager().getUserLogin().getEmail().equals(email)) {
            if (restaurantRequest.getName() != null &&
                    !restaurantRequest.getName().isEmpty() &&
                    !restaurantRequest.getName().equals(restaurant.getName())) {
                restaurant.setName(restaurantRequest.getName());
            }
            if (restaurantRequest.getShortDescription() != null &&
                    !restaurantRequest.getShortDescription().isEmpty() &&
                    !restaurantRequest.getShortDescription().equals(restaurant.getShortDescription())) {
                restaurant.setShortDescription(restaurantRequest.getShortDescription());
            }
            if (restaurantRequest.getProfilePicture() != null &&
                    !restaurantRequest.getProfilePicture().isEmpty() &&
                    !restaurantRequest.getProfilePicture().equals(restaurant.getProfilePicture())) {
                restaurant.setProfilePicture(restaurantRequest.getProfilePicture());
            }
        } else {
            throw new UnauthorizedException("User doesn't have permission");
        }
    }

    public void deleteRestaurantById(String bearerToken, Long id) {
        log.info("Deleting restaurant with given id");
        String token = bearerToken.substring(7);
        String email = jwtService.extractUsername(token);
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Restaurant not found"));
        if (restaurant.getManager().getUserLogin().getEmail().equals(email)) {
            restaurantRepository.deleteById(id);
        } else {
            throw new UnauthorizedException("User doesn't have permission");
        }
    }

    public void deleteRestaurantPictureById(String bearerToken, Long restaurantId, Long pictureId) {
        log.info("Deleting restaurant picture with given id");
        RestaurantPicture restaurantPicture = restaurantPictureRepository.findById(pictureId)
                .orElseThrow(() -> new NotFoundException("Picture not found"));
        if (!restaurantPicture.getRestaurant().getId().equals(restaurantId)) {
            throw new NotFoundException("Restaurant not found");
        }
        String token = bearerToken.substring(7);
        String email = jwtService.extractUsername(token);
        if (restaurantPicture.getRestaurant().getManager().getUserLogin().getEmail().equals(email)) {
            restaurantPictureRepository.deleteById(pictureId);
        } else {
            throw new UnauthorizedException("User doesn't have permission");
        }
    }
}
