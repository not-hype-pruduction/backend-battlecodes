package com.battlecodes.backend.models;

import org.springframework.security.core.GrantedAuthority;

public enum ERole implements GrantedAuthority {
    ROLE_STUDENT,
    ROLE_TEACHER,
    ROLE_ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
