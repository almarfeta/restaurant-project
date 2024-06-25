package com.example.project_server.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "restaurantPicture")
@Table(name = "restaurant_picture")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantPicture {

    @Id
    @SequenceGenerator(name = "restaurantPictureSequence", sequenceName = "restaurant_picture_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "restaurantPictureSequence")
    @Column(updatable = false)
    private Long id;

    @Column(nullable = false)
    private String picture;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private Restaurant restaurant;
}
