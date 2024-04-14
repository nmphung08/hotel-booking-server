package com.nmphung.hotelbooking.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingDto {
    private Long id;
    private String owner;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate checkInDate;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate checkOutDate;
    private Integer adults;
    private Integer children;
    private String confirmationCode;
    private RoomDto room;
}
