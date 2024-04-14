package com.nmphung.hotelbooking.services.impl;

import com.nmphung.hotelbooking.domain.entities.RoleEntity;
import com.nmphung.hotelbooking.domain.entities.UserEntity;
import com.nmphung.hotelbooking.repositories.IRoleRepository;
import com.nmphung.hotelbooking.repositories.IUserRepository;
import com.nmphung.hotelbooking.services.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService implements IUserService {
    private IUserRepository userRepository;
    private IRoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    public UserEntity registerUser(UserEntity userEntity) {
        RoleEntity roleUser = roleRepository.findByName("ROLE_USER").get();
        roleUser.getUsers().add(userEntity);
        userEntity.getRoles().add(roleUser);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        return userRepository.save(userEntity);
    }

    @Override
    public boolean isExisting(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public void deleteByUsername(String username) {
        userRepository.deleteByUsername(username);
    }
}
