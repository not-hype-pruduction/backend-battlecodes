package com.battlecodes.backend.repositories;

import com.battlecodes.backend.models.UserAlgorithm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserAlgorithmRepositories extends JpaRepository<UserAlgorithm, Long> {

    List<UserAlgorithm> findAllByGroupId(Long groupId);

}
