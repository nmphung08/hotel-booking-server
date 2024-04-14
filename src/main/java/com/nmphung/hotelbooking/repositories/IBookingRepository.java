package com.nmphung.hotelbooking.repositories;

import com.nmphung.hotelbooking.domain.entities.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IBookingRepository extends JpaRepository<BookingEntity, Long> {

    @Query("SELECT b from BookingEntity b WHERE b.confirmationCode = ?1")
    Optional<BookingEntity> findByConfirmationCode(String code);
}
