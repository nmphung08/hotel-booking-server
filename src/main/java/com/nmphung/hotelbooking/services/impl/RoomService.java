package com.nmphung.hotelbooking.services.impl;

import com.nmphung.hotelbooking.domain.entities.BookingEntity;
import com.nmphung.hotelbooking.domain.entities.RoomEntity;
import com.nmphung.hotelbooking.repositories.IRoomRepository;
import com.nmphung.hotelbooking.services.IRoomService;
import com.nmphung.hotelbooking.utils.CommonUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoomService implements IRoomService {
    private final IRoomRepository roomRepository;
    private final CommonUtils utils;

    public RoomService(IRoomRepository roomRepository, CommonUtils utils) {
        this.roomRepository = roomRepository;
        this.utils = utils;
    }

    @Override
    public RoomEntity addRoom(String roomType, BigDecimal roomPrice, MultipartFile photo) throws IOException, SQLException {
        RoomEntity roomEntity = RoomEntity.builder()
                .roomType(roomType)
                .roomPrice(roomPrice)
                .build();
        roomEntity.setPhoto(utils.multipartToBlob(photo));
        return roomRepository.save(roomEntity);
    }

    @Override
    public List<RoomEntity> getAllRooms() {
        return roomRepository.findAll();
    }

    @Override
    public Optional<RoomEntity> getRoom(Long id) {
        return roomRepository.findById(id);
    }

    @Override
    public List<String> getAllRoomTypes() {
        return roomRepository.getAllRoomTypes();
    }

    @Override
    public List<RoomEntity> getAvailableRooms(String roomType, LocalDate checkInDate, LocalDate checkOutDate) {
        List<RoomEntity> roomEntities = this.getAllRooms();
        return roomEntities.stream().filter((room) -> {
            boolean roomTypeEqual = roomType.isEmpty() || room.getRoomType().equalsIgnoreCase(roomType);
            return roomTypeEqual && this.isRoomAvailable(room.getId(), checkInDate, checkOutDate);
        }).collect(Collectors.toList());
    }

    @Override
    public RoomEntity updateRoom(Long id, String roomType, BigDecimal roomPrice, MultipartFile photo) throws IOException, SQLException {
        RoomEntity roomEntity = roomRepository.findById(id).get();
        roomEntity.setRoomType(roomType);
        roomEntity.setRoomPrice(roomPrice);
        roomEntity.setPhoto(utils.multipartToBlob(photo));
        return roomRepository.save(roomEntity);
    }

    @Override
    public RoomEntity updateRoomPartially(Long id, String roomType, BigDecimal roomPrice, MultipartFile photo) throws SQLException, IOException {
        RoomEntity roomEntity = roomRepository.findById(id).get();
        Optional.ofNullable(roomType).ifPresent(roomEntity::setRoomType);
        Optional.ofNullable(roomPrice).ifPresent(roomEntity::setRoomPrice);
        Optional.ofNullable(utils.multipartToBlob(photo)).ifPresent(roomEntity::setPhoto);
        return roomRepository.save(roomEntity);
    }

    @Override
    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }

    @Override
    public boolean isExisting(Long roomId) {
        return roomRepository.existsById(roomId);
    }

    @Override
    public boolean isRoomAvailable(Long roomId, LocalDate checkIn, LocalDate checkOut) {
        List<BookingEntity> bookingEntities = roomRepository.findById(roomId).get().getBookings();
        for (BookingEntity entity : bookingEntities) {
            LocalDate bookingCheckIn = entity.getCheckInDate();
            LocalDate bookingCheckOut = entity.getCheckOutDate();
            if (
                    ((checkIn.isAfter(bookingCheckIn) || checkIn.isEqual(bookingCheckIn))
                            && (checkIn.isBefore(bookingCheckOut) || checkIn.isEqual(bookingCheckOut))
                    )
                    || ((checkOut.isAfter(bookingCheckIn) || checkOut.isEqual(bookingCheckIn))
                            && (checkOut.isBefore(bookingCheckOut) || checkOut.isEqual(bookingCheckOut))
                    )
            ) {
                return false;
            }
        }
        return true;
    }
}
