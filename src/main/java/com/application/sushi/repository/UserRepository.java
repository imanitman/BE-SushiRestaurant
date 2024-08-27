package com.application.sushi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.application.sushi.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User save(User user);
    User findByEmail(String email);
    boolean existsByEmail(String email);
}
