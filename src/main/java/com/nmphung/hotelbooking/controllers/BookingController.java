package com.nmphung.hotelbooking.controllers;

import com.nmphung.hotelbooking.domain.dto.BookingDto;
import com.nmphung.hotelbooking.domain.entities.BookingEntity;
import com.nmphung.hotelbooking.mappers.IMapper;
import com.nmphung.hotelbooking.services.IBookingService;
import com.nmphung.hotelbooking.services.IRoomService;
import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private IMapper<BookingEntity, BookingDto> bookingMapper;
    private IBookingService bookingService;
    private IRoomService roomService;

    @PostMapping
    @PreAuthorize("#bookingDto.owner == authentication.principal.username")
    public ResponseEntity<?> addBooking(
            @RequestParam Long roomId,
            @RequestBody BookingDto bookingDto
    ) {
        if (!roomService.isExisting(roomId)) {
            return ResponseEntity.badRequest().body("Cannot find room with corresponding ID");
        }
        BookingEntity bookingEntity = bookingMapper.mapFrom(bookingDto);
        LocalDate checkInDate = bookingEntity.getCheckInDate();
        LocalDate checkOutDate = bookingEntity.getCheckOutDate();
        if (checkInDate.isAfter(checkOutDate)) {
            return ResponseEntity.badRequest().body("Invalid dates");
        }
        if (!roomService.isRoomAvailable(roomId, checkInDate, checkOutDate)) {
            return ResponseEntity.badRequest().body("Room is not available");
        }
        BookingEntity newBookingEntity = bookingService.addBooking(roomId, bookingEntity);
        return new ResponseEntity<>(bookingMapper.mapTo(newBookingEntity), HttpStatus.CREATED);
    }

    @GetMapping
    @RolesAllowed("ADMIN")
    public List<BookingDto> getAllBookings() {
        List<BookingEntity> bookingEntities = bookingService.getAllBookings();
        return bookingEntities.stream().map(bookingMapper::mapTo).collect(Collectors.toList());
    }

    @GetMapping(params = "username")
    @PreAuthorize("#username == authentication.principal.username")
    public List<BookingDto> getUserBookings(@RequestParam String username) {
        List<BookingEntity> bookingEntities = bookingService.getUserBookings(username);
        return bookingEntities.stream().map(bookingMapper::mapTo).collect(Collectors.toList());
    }

    @GetMapping(params = "confirmation-code")
    public ResponseEntity<BookingDto> getBookingByCode(@RequestParam(name = "confirmation-code") String confirmationCode) {
        Optional<BookingEntity> bookingEntity = bookingService.getBookingByCode(confirmationCode);
        return bookingEntity.map(booking -> ResponseEntity.ok(bookingMapper.mapTo(booking))).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<?> deleteBooking(@PathVariable Long id) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            bookingService.deleteBooking(id, username);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}
