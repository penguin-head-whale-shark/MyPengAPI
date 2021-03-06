package com.github.pdgs.MyPengAPI.account.repository;

import com.github.pdgs.MyPengAPI.account.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserJpaRepo extends JpaRepository<User, Long> {

    Optional<User> findById(String email);
}
