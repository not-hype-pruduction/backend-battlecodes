package com.battlecodes.backend.services;

import com.battlecodes.backend.configurations.UserDetailsImpl;
import com.battlecodes.backend.models.UserModel;
import com.battlecodes.backend.repositories.UserRepositories;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepositories userRepositories;
    private final PasswordEncoder passwordEncoder;

    public Boolean createUser(UserModel user) {
        if(userRepositories.existsByEmail(user.getEmail())){
            return false;
        }

        userRepositories.save(user);

        return true;
    }

    public UserModel getUserByEmail(String email) {
        return userRepositories.findByEmail(email).orElse(null);
    }

    public String getStoredPassword(String email) {
        return userRepositories.findByEmail(email).get().getPassword();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel user = userRepositories.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));
        return UserDetailsImpl.build(user);
    }
}
