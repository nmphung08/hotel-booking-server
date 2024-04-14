package com.nmphung.hotelbooking.mappers.impl;

import com.nmphung.hotelbooking.domain.dto.RoomDto;
import com.nmphung.hotelbooking.domain.entities.RoomEntity;
import com.nmphung.hotelbooking.mappers.IMapper;
import com.nmphung.hotelbooking.utils.CommonUtils;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
@AllArgsConstructor
public class RoomMapper implements IMapper<RoomEntity, RoomDto> {
    private ModelMapper modelMapper;
    private CommonUtils utils;

    @Override
    public RoomDto mapTo(RoomEntity roomEntity) {
        RoomDto roomDto = modelMapper.map(roomEntity, RoomDto.class);
        try {
            roomDto.setPhoto(utils.blobToString(roomEntity.getPhoto()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return roomDto;
    }

    @Override
    public RoomEntity mapFrom(RoomDto roomDto) {
        RoomEntity roomEntity = modelMapper.map(roomDto, RoomEntity.class);
        try {
            roomEntity.setPhoto(utils.stringToBlob(roomDto.getPhoto()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return roomEntity;
    }
}
