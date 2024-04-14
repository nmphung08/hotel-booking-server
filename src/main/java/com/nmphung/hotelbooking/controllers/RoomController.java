package com.nmphung.hotelbooking.controllers;


import com.nmphung.hotelbooking.domain.dto.RoomDto;
import com.nmphung.hotelbooking.domain.entities.RoomEntity;
import com.nmphung.hotelbooking.mappers.IMapper;
import com.nmphung.hotelbooking.services.IRoomService;
import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping(path =  "/rooms")
public class RoomController  {

    private IMapper<RoomEntity, RoomDto> roomMapper;
    private IRoomService roomService;

    @PostMapping
    @RolesAllowed("ADMIN")
    public ResponseEntity<RoomDto> addRoom(
            @RequestParam(required = false) String roomType,
            @RequestParam(required = false) BigDecimal roomPrice,
            @RequestParam(required = false) MultipartFile photo
            ) throws SQLException, IOException {
        RoomEntity roomEntity = roomService.addRoom(roomType, roomPrice, photo);
        RoomDto roomDto = roomMapper.mapTo(roomEntity);
        return new ResponseEntity<>(roomDto, HttpStatus.CREATED);
    }

    @GetMapping
    public List<RoomDto> getAllRooms() {
        List<RoomEntity> roomEntities = roomService.getAllRooms();
        return roomEntities.stream().map(roomMapper::mapTo).collect(Collectors.toList());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<RoomDto> getRoom(@PathVariable Long id) {
        Optional<RoomEntity> roomEntity = roomService.getRoom(id);
        return roomEntity.map(entity -> ResponseEntity.ok(roomMapper.mapTo(entity))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(path = "/types")
    public List<String> getAllRoomTypes() {
        return roomService.getAllRoomTypes();
    }

    @GetMapping(params = {"room-type", "check-in", "check-out"})
    public List<RoomDto> getAvailableRooms(
            @RequestParam(name = "room-type") String roomType,
            @RequestParam(name = "check-in") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate checkInDate,
            @RequestParam(name = "check-out") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate checkOutDate
            ) {
        if (checkInDate.isAfter(checkOutDate) || checkInDate.isEqual(checkOutDate)) {
            return new ArrayList<>();
        }
        List<RoomEntity> roomEntities = roomService.getAvailableRooms(roomType, checkInDate, checkOutDate);
        return roomEntities.stream().map(roomMapper::mapTo).collect(Collectors.toList());
    }

    @PutMapping(path = "/{id}")
    @RolesAllowed("ADMIN")
    public ResponseEntity<?> updateRoom(
            @PathVariable Long id,
            @RequestParam(required = false) String roomType,
            @RequestParam(required = false) BigDecimal roomPrice,
            @RequestParam(required = false) MultipartFile photo
    ) throws SQLException, IOException {
        if (!roomService.isExisting(id)) {
            return ResponseEntity.badRequest().body("Cannot find room with corresponding ID");
        }
        RoomEntity roomEntity = roomService.updateRoom(id, roomType, roomPrice, photo);
        RoomDto roomDto = roomMapper.mapTo(roomEntity);
        return ResponseEntity.ok(roomDto);
    }

    @PatchMapping(path = "/{id}")
    @RolesAllowed("ADMIN")
    public ResponseEntity<?> updateRoomPartially(
            @PathVariable Long id,
            @RequestParam(required = false) String roomType,
            @RequestParam(required = false) BigDecimal roomPrice,
            @RequestParam(required = false) MultipartFile photo
    ) throws SQLException, IOException {
        if (!roomService.isExisting(id)) {
            return ResponseEntity.badRequest().body("Cannot find room with corresponding ID");
        }
        RoomEntity roomEntity = roomService.updateRoomPartially(id, roomType, roomPrice, photo);
        RoomDto roomDto = roomMapper.mapTo(roomEntity);
        return ResponseEntity.ok(roomDto);
    }

    @DeleteMapping(path =  "/{id}")
    @RolesAllowed("ADMIN")
    public ResponseEntity<?> deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.noContent().build();
    }
}
