package com.nmphung.hotelbooking.controllers;

import com.nmphung.hotelbooking.services.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private IUserService userService;

    @DeleteMapping(path = "/{username}")
    @PreAuthorize("#username == authentication.principal.username")
    @Transactional
    public ResponseEntity<?> deleteUser(@PathVariable String username) {
        userService.deleteByUsername(username);
        return ResponseEntity.noContent().build();
    }
}
