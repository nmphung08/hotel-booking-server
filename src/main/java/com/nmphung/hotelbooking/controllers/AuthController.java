package com.nmphung.hotelbooking.controllers;

import com.nmphung.hotelbooking.domain.dto.JwtDto;
import com.nmphung.hotelbooking.domain.dto.UserDto;
import com.nmphung.hotelbooking.domain.entities.UserEntity;
import com.nmphung.hotelbooking.mappers.IMapper;
import com.nmphung.hotelbooking.request.LoginRequest;
import com.nmphung.hotelbooking.security.user.HotelUserDetails;
import com.nmphung.hotelbooking.services.IUserService;
import com.nmphung.hotelbooking.utils.JwtUtils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/auth")
public class AuthController {

    private IUserService userService;
    private IMapper<UserEntity, UserDto> userMapper;
    private AuthenticationManager authenticationManager;
    private JwtUtils jwtUtils;

    @PostMapping(path =  "/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto) {
        if (userService.isExisting(userDto.getUsername())) {
            return ResponseEntity.badRequest().body("Username had already existed");
        }
        UserEntity userEntity = userService.registerUser(userMapper.mapFrom(userDto));
        return new ResponseEntity<>(userMapper.mapTo(userEntity), HttpStatus.CREATED);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest req) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                req.getUsername(), req.getPassword()
        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        HotelUserDetails userDetails = (HotelUserDetails) authentication.getPrincipal();

        String jwt = jwtUtils.generateJwtTokenForUser(authentication);
        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        JwtDto jwtDto = JwtDto.builder()
                .id(userDetails.getId())
                .username(userDetails.getUsername())
                .roles(roles)
                .jwt(jwt)
                .build();
        return ResponseEntity.ok(jwtDto);
    }
}
