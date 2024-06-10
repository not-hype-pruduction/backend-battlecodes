package com.battlecodes.backend.models.requests;

import com.battlecodes.backend.models.ERole;
import lombok.Data;

import java.util.Set;

@Data
public class RegisterUserRequest {
    private String name;
    private String email;
    private String password;
    private Set<ERole> roles;
}
