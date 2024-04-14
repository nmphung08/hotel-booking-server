package com.nmphung.hotelbooking.services;

import com.nmphung.hotelbooking.domain.entities.RoomEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IRoomService {

    RoomEntity addRoom(String roomType, BigDecimal roomPrice, MultipartFile photo) throws IOException, SQLException;

    List<RoomEntity> getAllRooms();

    Optional<RoomEntity> getRoom(Long id);

    List<String> getAllRoomTypes();

    List<RoomEntity> getAvailableRooms(String roomType, LocalDate checkInDate, LocalDate checkOutDate);

    RoomEntity updateRoom(Long id, String roomType, BigDecimal roomPrice, MultipartFile photo) throws IOException, SQLException;

    RoomEntity updateRoomPartially(Long id, String roomType, BigDecimal roomPrice, MultipartFile photo) throws SQLException, IOException;

    void deleteRoom(Long id);

    boolean isExisting(Long roomId);

    boolean isRoomAvailable(Long roomId, LocalDate checkIn, LocalDate checkOut);
}
