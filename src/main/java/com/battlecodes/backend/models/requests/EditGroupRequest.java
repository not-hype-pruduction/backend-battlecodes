package com.battlecodes.backend.models.requests;

import lombok.Data;

@Data
public class EditGroupRequest {
    private String name;
    private String game;
    private String password;
}
