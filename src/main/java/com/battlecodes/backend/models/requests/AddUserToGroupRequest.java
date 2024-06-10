package com.battlecodes.backend.models.requests;

import lombok.Data;

@Data
public class AddUserToGroupRequest {
    private String name;
    private String password;
}
