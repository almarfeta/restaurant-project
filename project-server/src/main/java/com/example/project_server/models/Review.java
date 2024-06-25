package com.example.project_server.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity(name = "review")
@Table(name = "review")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    @Id
    @SequenceGenerator(name = "reviewSequence", sequenceName = "review_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reviewSequence")
    @Column(updatable = false)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Integer rating;

    @Column(nullable = false, length = 2000)
    private String note;

    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference("review_ref1")
    private UserProfile author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference("review_ref2")
    private Restaurant restaurant;

    @ManyToMany(mappedBy = "upVotedReviews")
    @JsonBackReference("upVote_ref2")
    private List<UserProfile> upVotedByUsers;

    @Transient
    private int numberOfUpVotes;

    public int getNumberOfUpVotes() {
        return upVotedByUsers == null ? 0 : upVotedByUsers.size();
    }
}
