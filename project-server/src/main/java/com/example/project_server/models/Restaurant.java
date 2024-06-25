package com.example.project_server.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity(name = "restaurant")
@Table(name = "restaurant")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {

    @Id
    @SequenceGenerator(name = "restaurantSequence", sequenceName = "restaurant_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "restaurantSequence")
    @Column(updatable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String profilePicture;

    @Column(length = 1000)
    private String shortDescription;

    @Column(nullable = false)
    private LocalDate since;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference("restaurant_ref2")
    private UserProfile manager;

    @OneToMany(mappedBy = "restaurant", orphanRemoval = true)
    private List<Review> reviews;

    @OneToMany(mappedBy = "restaurant", orphanRemoval = true)
    private List<RestaurantPicture> pictures;

    @Transient
    private int numberOfReviews;

    @Transient
    private double rating;


    public int getNumberOfReviews() {
        return reviews == null ? 0 : reviews.size();
    }

    public double getRating() {
        return reviews == null ? 0 :
                reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0);
    }
}
