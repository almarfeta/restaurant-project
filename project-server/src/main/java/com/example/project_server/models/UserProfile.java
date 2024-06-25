package com.example.project_server.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity(name = "userProfile")
@Table(name = "user_profile")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {

    @Id
    @SequenceGenerator(name = "userProfileSequence", sequenceName = "user_profile_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userProfileSequence")
    @Column(updatable = false)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private LocalDate joinDate;

    @OneToMany(mappedBy = "manager")
    private List<Restaurant> restaurantsManaged;

    @OneToOne(mappedBy = "userProfile")
    @JsonBackReference
    private UserLogin userLogin;

    @OneToMany(mappedBy = "author")
    private List<Review> reviews;

    @ManyToMany
    @JoinTable(
            name = "up_vote",
            joinColumns = @JoinColumn(name = "up_voter_id"),
            inverseJoinColumns = @JoinColumn(name = "review_id")
    )
    @JsonBackReference("upVote_ref1")
    private List<Review> upVotedReviews;
}
