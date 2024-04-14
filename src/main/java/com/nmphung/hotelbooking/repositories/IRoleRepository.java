package com.nmphung.hotelbooking.repositories;

import com.nmphung.hotelbooking.domain.entities.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IRoleRepository extends JpaRepository<RoleEntity, Long> {
    public Optional<RoleEntity> findByName(String name);
}
