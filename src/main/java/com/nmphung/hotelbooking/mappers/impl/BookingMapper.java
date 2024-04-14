package com.nmphung.hotelbooking.mappers.impl;

import com.nmphung.hotelbooking.domain.dto.BookingDto;
import com.nmphung.hotelbooking.domain.entities.BookingEntity;
import com.nmphung.hotelbooking.mappers.IMapper;
import com.nmphung.hotelbooking.utils.CommonUtils;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
@AllArgsConstructor
public class BookingMapper implements IMapper<BookingEntity, BookingDto> {
    private ModelMapper modelMapper;
    private CommonUtils utils;

    @Override
    public BookingDto mapTo(BookingEntity bookingEntity) {
        BookingDto bookingDto = modelMapper.map(bookingEntity, BookingDto.class);
        try {
            bookingDto.getRoom().setPhoto(
                    utils.blobToString(
                            bookingEntity.getRoom().getPhoto()
                    )
            );
            return bookingDto;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public BookingEntity mapFrom(BookingDto bookingDto) {
        return modelMapper.map(bookingDto, BookingEntity.class);
    }
}
