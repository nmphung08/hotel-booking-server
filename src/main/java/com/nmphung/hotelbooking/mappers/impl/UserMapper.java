package com.nmphung.hotelbooking.mappers.impl;

import com.nmphung.hotelbooking.domain.dto.UserDto;
import com.nmphung.hotelbooking.domain.entities.UserEntity;
import com.nmphung.hotelbooking.mappers.IMapper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserMapper implements IMapper<UserEntity, UserDto> {
    private ModelMapper modelMapper;

    @Override
    public UserDto mapTo(UserEntity userEntity) {
        return modelMapper.map(userEntity, UserDto.class);
    }

    @Override
    public UserEntity mapFrom(UserDto userDto) {
        return modelMapper.map(userDto, UserEntity.class);
    }
}
