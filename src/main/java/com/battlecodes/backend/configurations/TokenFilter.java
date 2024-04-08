package com.battlecodes.backend.configurations;

import com.battlecodes.backend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenFilter {
    private final JwtCore jwtCore;
    private final UserDetailsService userDetailsService;
}
