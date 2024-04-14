package com.nmphung.hotelbooking.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class JwtDto {
    private Long id;
    private String username;
    private List<String> roles;
    private String jwt;
}
