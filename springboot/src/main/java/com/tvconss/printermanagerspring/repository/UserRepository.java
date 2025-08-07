package com.tvconss.printermanagerspring.repository;

import com.tvconss.printermanagerspring.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUserId(Long userId);

    Optional<UserEntity> findByUserEmail(String email);

}
