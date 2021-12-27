package com.github.pdgs.MyPengAPI.repository;

import com.github.pdgs.MyPengAPI.domain.posts.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepo extends JpaRepository<User, Long> {

    Optional<User> findById(String email);
}
