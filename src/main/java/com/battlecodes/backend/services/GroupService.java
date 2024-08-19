package com.battlecodes.backend.services;

import com.battlecodes.backend.models.GroupModel;
import com.battlecodes.backend.models.UserModel;
import com.battlecodes.backend.repositories.GroupRepositories;
import com.battlecodes.backend.repositories.UserRepositories;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepositories groupsRepositories;
    private final UserRepositories userRepositories;

    public boolean createGroup(String name, String password, String game, Long userId) {
        log.info("Creating group {}", name);

        if (groupsRepositories.findByName(name).isPresent()) {
            return false;
        }

        GroupModel group = new GroupModel(name, password, game, userId);
        groupsRepositories.save(group);
        return true;
    }

    public boolean addUserToGroup(String name, String password, Long userId) {
        log.info("Adding user {} to group {}", userId, name);
        Optional<GroupModel> groupOptional = groupsRepositories.findByName(name);
        Optional<UserModel> userOptional = userRepositories.findById(userId);

        if (groupOptional.isEmpty() || userOptional.isEmpty()) {
            return false;
        }

        GroupModel group = groupOptional.get();
        UserModel user = userOptional.get();

        if (group.getUsers().contains(user)) {
            return false;
        }

        if (!group.getPassword().equals(password)) {
            return false;
        }

        group.addUser(user);
        groupsRepositories.save(group);
        userRepositories.save(user);
        return true;
    }

    public boolean removeUserFromGroup(String name, Long userId) {
        log.info("Removing user {} from group {}", userId, name);
        Optional<GroupModel> groupOptional = groupsRepositories.findByName(name);
        Optional<UserModel> userOptional = userRepositories.findById(userId);

        if (groupOptional.isEmpty() || userOptional.isEmpty()) {
            return false;
        }

        GroupModel group = groupOptional.get();
        UserModel user = userOptional.get();

        if (!group.getUsers().contains(user)) {
            return false;
        }

        group.deleteUser(user);
        groupsRepositories.save(group);
        userRepositories.save(user);
        return true;
    }

    public List<GroupModel> getUserGroups(Long userId) {
        return groupsRepositories.findAllByCreatesId(userId).orElse(null);
    }

    public GroupModel getGroupByName(String name){
        return groupsRepositories.findByName(name).orElse(null);
    }

    public GroupModel getGroupById(Long id){
        return groupsRepositories.findById(id).orElse(null);
    }

    public List<GroupModel> getAllGroups(){
        return groupsRepositories.findAll();
    }

    public boolean removeGroup(String name, Long userId) {
        log.info("Removing group {}", name);
        Optional<GroupModel> groupOptional = groupsRepositories.findByName(name);

        if (groupOptional.isEmpty()) {
            return false;
        }

        GroupModel group = groupOptional.get();

        if (!group.getCreatesId().equals(userId)) {
            return false;
        }

        for (UserModel user : group.getUsers()) {
            user.setGroup(null);
            userRepositories.save(user);
        }

        groupsRepositories.delete(group);
        return true;
    }

    public boolean editGroup(Long id, String name, String game, String password) {
        GroupModel group = groupsRepositories.findById(id).orElse(null);

        if (group == null) {
            return false;
        }

        if (name != null && !name.isEmpty()) {
            group.setName(name);
        }

        if (game != null && !game.isEmpty()) {
            group.setGame(game);
        }

        if (password != null && !password.isEmpty()) {
            group.setPassword(password);
        }

        groupsRepositories.save(group);
        return true;
    }

    public String getGameGroup(Long id){
        return groupsRepositories.findById(id).orElse(null).getGame();
    }
}
