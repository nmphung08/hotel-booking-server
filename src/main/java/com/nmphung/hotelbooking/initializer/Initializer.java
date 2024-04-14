package com.nmphung.hotelbooking.initializer;

import com.nmphung.hotelbooking.domain.entities.RoleEntity;
import com.nmphung.hotelbooking.domain.entities.UserEntity;
import com.nmphung.hotelbooking.repositories.IRoleRepository;
import com.nmphung.hotelbooking.repositories.IUserRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

@Component
@AllArgsConstructor
// a initializer create necessary data: roles, admin account
public class Initializer {
    private IRoleRepository roleRepository;
    private IUserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        initializeRoles();
        initializeAdmin();
        assignRoleToAdmin();
    }

    private void initializeRoles() {
        Optional<RoleEntity> adminRole = roleRepository.findByName("ROLE_ADMIN");
        if (adminRole.isEmpty()) {
            RoleEntity adminRoleEntity = RoleEntity.builder()
                    .name("ROLE_ADMIN")
                    .build();
            roleRepository.save(adminRoleEntity);
        }

        Optional<RoleEntity> userRole = roleRepository.findByName("ROLE_USER");
        if (userRole.isEmpty()) {
            RoleEntity userRoleEntity = RoleEntity.builder()
                    .name("ROLE_USER")
                    .build();
            roleRepository.save(userRoleEntity);
        }
    }

    private void initializeAdmin() {
        Optional<UserEntity> adminUser = userRepository.findByUsername("admin");
        if (adminUser.isEmpty()) {
            UserEntity adminUserEntity = UserEntity.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin"))
                    .build();
            userRepository.save(adminUserEntity);
        }
    }

    private void assignRoleToAdmin() {
        Optional<UserEntity> adminUser = userRepository.findByUsername("admin");
        RoleEntity adminRole = roleRepository.findByName("ROLE_ADMIN").get();

        UserEntity adminEntity = adminUser.get();
        Collection<RoleEntity> adminRoles = adminEntity.getRoles();
        boolean haveAdminRole = false;
        for (RoleEntity role : adminRoles) {
            if (role.getName().equals("ROLE_ADMIN")) {
                haveAdminRole = true;
                break;
            }
        }
        if (!haveAdminRole) {
            adminRoles.add(adminRole);
        }
        userRepository.save(adminEntity);
    }
}
