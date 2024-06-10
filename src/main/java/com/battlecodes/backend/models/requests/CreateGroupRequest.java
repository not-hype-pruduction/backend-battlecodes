package com.battlecodes.backend.models.requests;

import lombok.Data;

@Data
public class CreateGroupRequest {
    private String groupName;
    private String password;
    private String game;
}
