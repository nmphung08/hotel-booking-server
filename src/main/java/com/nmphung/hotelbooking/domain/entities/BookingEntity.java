package com.nmphung.hotelbooking.domain.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "booking")
public class BookingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String owner;

    @Column(name = "check-in-date")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate checkInDate;

    @Column(name = "check-out-date")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate checkOutDate;

    private Integer adults;

    private Integer children;

    @Column(name = "confirmation-code")
    private String confirmationCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room-id")
    private RoomEntity room;
}
