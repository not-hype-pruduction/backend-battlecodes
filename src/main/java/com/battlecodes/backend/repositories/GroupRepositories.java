package com.battlecodes.backend.repositories;

import com.battlecodes.backend.models.GroupModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupRepositories extends JpaRepository<GroupModel, Integer> {
    Optional<GroupModel> findById(Long id);
    Optional<GroupModel> findByName(String name);
    Optional<List<GroupModel>> findAllByCreatesId(Long id);
}
