package com.nmphung.hotelbooking.services.impl;

import com.nmphung.hotelbooking.domain.entities.BookingEntity;
import com.nmphung.hotelbooking.domain.entities.RoomEntity;
import com.nmphung.hotelbooking.repositories.IBookingRepository;
import com.nmphung.hotelbooking.repositories.IRoomRepository;
import com.nmphung.hotelbooking.services.IBookingService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingService implements IBookingService {
    private IBookingRepository bookingRepository;
    private IRoomRepository roomRepository;

    public BookingService(IBookingRepository bookingRepository, IRoomRepository roomRepository) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
    }

    @Override
    public BookingEntity addBooking(Long roomId, BookingEntity bookingEntity) {
        RoomEntity roomEntity = roomRepository.findById(roomId).get();
        roomEntity.getBookings().add(bookingEntity);
        bookingEntity.setRoom(roomEntity);
        String confirmationCode = RandomStringUtils.randomNumeric(8);
        bookingEntity.setConfirmationCode(confirmationCode);
        return bookingRepository.save(bookingEntity);
    }

    @Override
    public List<BookingEntity> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public List<BookingEntity> getUserBookings(String username) {
        List<BookingEntity> bookingEntities = bookingRepository.findAll();
        return bookingEntities
                .stream()
                .filter(booking -> booking.getOwner().equals(username))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BookingEntity> getBookingByCode(String code) {
        return bookingRepository.findByConfirmationCode(code);
    }

    @Override
    public void deleteBooking(Long id, String username) {
        Optional<BookingEntity> bookingEntity = bookingRepository.findById(id);
        if (bookingEntity.isPresent()) {
            if (bookingEntity.get().getOwner().equals(username)) {
                bookingRepository.deleteById(id);
            } else {
                throw new RuntimeException("Not allow to cancel other's booking");
            }
        }
    }
}
