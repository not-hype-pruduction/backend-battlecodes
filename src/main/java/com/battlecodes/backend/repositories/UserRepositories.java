package com.battlecodes.backend.repositories;

import com.battlecodes.backend.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepositories extends JpaRepository<UserModel, Long> {

    Optional<UserModel> findByEmail(String username);

    Boolean existsByEmail(String email);
}
