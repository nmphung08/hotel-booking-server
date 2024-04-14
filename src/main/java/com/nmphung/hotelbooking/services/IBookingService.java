package com.nmphung.hotelbooking.services;

import com.nmphung.hotelbooking.domain.entities.BookingEntity;

import java.util.List;
import java.util.Optional;

public interface IBookingService {

    BookingEntity addBooking(Long roomId, BookingEntity bookingEntity);

    List<BookingEntity> getAllBookings();

    List<BookingEntity> getUserBookings(String username);

    Optional<BookingEntity> getBookingByCode(String code);

    void deleteBooking(Long id, String username);
}
