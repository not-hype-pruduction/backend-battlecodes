package com.battlecodes.backend.services;

import com.battlecodes.backend.configurations.UserDetailsImpl;
import com.battlecodes.backend.models.UserModel;
import com.battlecodes.backend.repositories.RefreshTokenRepository;
import com.battlecodes.backend.repositories.UserRepositories;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepositories userRepositories;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;

    public Boolean createUser(UserModel user) {
        if(userRepositories.existsByEmail(user.getEmail())){
            return false;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepositories.save(user);

        return true;
    }

    public UserModel getUserByEmail(String email) {
        return userRepositories.findByEmail(email).orElse(null);
    }

    public String getStoredPassword(String email) {
        return userRepositories.findByEmail(email).get().getPassword();
    }

    public List<UserModel> getAllUser() {
        return userRepositories.findAll();
    }

    @Transactional
    public boolean deleteUserById(Long id) {
        if (userRepositories.existsById(id)) {
            refreshTokenRepository.deleteByUserId(id); // Delete related refresh tokens
            userRepositories.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean editUser(Long id, String name, String email, String password) {
        UserModel user = userRepositories.findById(id).orElse(null);

        if (user == null) {
            return false;
        }

        if (name != null && !name.isEmpty()) {
            user.setName(name);
        }

        if (email != null && !email.isEmpty()) {
            user.setEmail(email);
        }

        if (password != null && !password.isEmpty()) {
            user.setPassword(passwordEncoder.encode(password));
        }

        userRepositories.save(user);
        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel user = userRepositories.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));
        return UserDetailsImpl.build(user);
    }
}
