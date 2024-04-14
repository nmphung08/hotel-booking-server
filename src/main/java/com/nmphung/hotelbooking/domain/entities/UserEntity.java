package com.nmphung.hotelbooking.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.HashSet;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE}
    )
    @JoinTable(name = "user-role",
            joinColumns = @JoinColumn(name = "user-id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role-id", referencedColumnName = "id")
    )
    private Collection<RoleEntity> roles = new HashSet<>();
}
