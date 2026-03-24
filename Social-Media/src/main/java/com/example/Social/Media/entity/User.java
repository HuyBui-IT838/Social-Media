package com.example.Social.Media.entity;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;

    // Social Graph: Follower & Following
    // Một User có thể follow nhiều người và được follow bởi nhiều người.
    
    @ManyToMany
    @JoinTable(
        name = "follows",
        joinColumns = @JoinColumn(name = "follower_id"),
        inverseJoinColumns = @JoinColumn(name = "following_id")
    )
    @Builder.Default
    @JsonIgnore
    private Set<User> following = new HashSet<>();

    @ManyToMany(mappedBy = "following")
    @Builder.Default
    @JsonIgnore
    private Set<User> followers = new HashSet<>();
}
