package com.nmphung.hotelbooking.repositories;

import com.nmphung.hotelbooking.domain.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByUsername(String username);

    Optional<UserEntity> findByUsername(String username);

    void deleteByUsername(String username);
}
