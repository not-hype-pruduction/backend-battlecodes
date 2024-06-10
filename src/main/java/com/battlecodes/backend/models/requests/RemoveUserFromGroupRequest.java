package com.battlecodes.backend.models.requests;

import lombok.Data;

@Data
public class RemoveUserFromGroupRequest {
    private String groupName;
    private Long userId;
}
