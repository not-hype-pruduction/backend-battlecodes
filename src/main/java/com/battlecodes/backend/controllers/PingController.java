package com.battlecodes.backend.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestController
@RequestMapping("/api")
@RestControllerAdvice
public class PingController {
    @GetMapping("/ping")
    public ResponseEntity<Object> ping(){
        return ResponseEntity.status(HttpStatus.OK).body("ok");
    }
}
