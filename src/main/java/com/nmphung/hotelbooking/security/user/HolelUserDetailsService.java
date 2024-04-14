package com.nmphung.hotelbooking.security.user;

import com.nmphung.hotelbooking.domain.entities.UserEntity;
import com.nmphung.hotelbooking.repositories.IUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class HolelUserDetailsService implements UserDetailsService {

    private final IUserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> user = userRepository.findByUsername(username);
        return user.map(HotelUserDetails::buildHotelUserDetails).orElseThrow(
                () -> new RuntimeException("Username not found!")
        );
    }
}
