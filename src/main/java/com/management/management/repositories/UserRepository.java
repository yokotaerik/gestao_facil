package com.management.management.repositories;

import com.management.management.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    UserDetails findByEmail(String email);


    Optional<User> findByUsername(String username);
}
