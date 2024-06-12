package com.battlecodes.backend.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "groups")
public class GroupModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name", unique = true)
    private String name;
    @Column(name = "password")
    private String password;
    @Column(name = "game")
    private String game;
    @Column(name = "creater_id")
    private Long createsId;
    @OneToMany(mappedBy = "group", cascade = CascadeType.DETACH)
    private List<UserModel> users = new ArrayList<>();

    public GroupModel(String name, String password, String game, Long userId) {
        this.name = name;
        this.password = password;
        this.game = game;
        this.createsId = userId;
    }

    public GroupModel() {

    }

    public void addUser(UserModel user){
        users.add(user);
        user.setGroup(this);
    }
    public void deleteUser(UserModel user){
        users.remove(user);
        user.setGroup(null);
    }
    public List<UserModel> listUser(){
        return users;
    }
}
