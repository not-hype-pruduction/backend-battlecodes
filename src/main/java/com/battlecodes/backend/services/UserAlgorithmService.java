package com.battlecodes.backend.services;

import com.battlecodes.backend.models.ERole;
import com.battlecodes.backend.models.GroupModel;
import com.battlecodes.backend.models.UserAlgorithm;
import com.battlecodes.backend.models.UserModel;
import com.battlecodes.backend.repositories.GroupRepositories;
import com.battlecodes.backend.repositories.UserAlgorithmRepositories;
import com.battlecodes.backend.repositories.UserRepositories;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserAlgorithmService {
    final private UserAlgorithmRepositories userAlgorithmRepositories;

    public boolean saveAlgorithm(UserModel user, byte[] bytes, String language) {
        log.info("Saving algorithm for user {}", user.getId());

        UserAlgorithm userAlgorithm = new UserAlgorithm();
        userAlgorithm.setUserId(user.getId());
        userAlgorithm.setGroupId(user.getGroup().getId());
        userAlgorithm.setBytes(bytes);
        userAlgorithm.setLanguage(language);

        userAlgorithmRepositories.save(userAlgorithm);
        return true;
    }

    public List<Long> getAlgorithmsByGroupId(Long groupId) {
        log.info("Getting algorithms for group {}", groupId);
        return userAlgorithmRepositories.findAllByGroupId(groupId).stream().map(UserAlgorithm::getId).collect(Collectors.toList());
    }
}
