package com.nmphung.hotelbooking.services;

import com.nmphung.hotelbooking.domain.entities.UserEntity;

public interface IUserService {
    UserEntity registerUser(UserEntity userEntity);

    boolean isExisting(String username);

    void deleteByUsername(String username);
}
