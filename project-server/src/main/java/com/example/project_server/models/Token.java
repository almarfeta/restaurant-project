package com.example.project_server.models;

import com.example.project_server.consts.TokenType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "token")
@Table(name = "token")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Token {

    @Id
    @SequenceGenerator(name = "tokenSequence", sequenceName = "token_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tokenSequence")
    @Column(updatable = false)
    public Long id;

    @Column(nullable = false, unique = true)
    public String token;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public TokenType tokenType;

    @Column(nullable = false)
    public boolean revoked;

    @Column(nullable = false)
    public boolean expired;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_login_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    public UserLogin userLogin;
}
