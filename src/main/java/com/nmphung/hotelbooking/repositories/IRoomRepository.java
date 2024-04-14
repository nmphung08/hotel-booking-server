package com.nmphung.hotelbooking.repositories;

import com.nmphung.hotelbooking.domain.entities.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IRoomRepository extends JpaRepository<RoomEntity, Long> {
    @Query("SELECT DISTINCT r.roomType from RoomEntity r")
    List<String> getAllRoomTypes();
}
