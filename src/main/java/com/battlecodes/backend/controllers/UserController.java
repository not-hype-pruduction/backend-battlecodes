package com.battlecodes.backend.controllers;

import com.battlecodes.backend.models.UserModel;
import com.battlecodes.backend.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RestControllerAdvice
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserModel user, BindingResult result) {

        if(result.hasErrors()) {
            return ResponseEntity.badRequest().body(result);
        }

        if(userService.createUser(user)){
            return ResponseEntity.ok().body(user);
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMyUser(Principal principal) {
        if(principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.ok().body(principal.getName());
    }

}
