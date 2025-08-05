package com.tvconss.printermanagerspring.repository;

import com.tvconss.printermanagerspring.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByUserEmail(String email);

}
