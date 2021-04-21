package com.myserver.api.controller;

import com.myserver.api.dto.response.AuthResponse;
import com.myserver.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<AuthResponse> login(@RequestParam String login, @RequestParam String password) {
        final AuthResponse authResponse = userService.login(login, password);
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }
}
