package com.financialtools.controller;

import com.financialtools.model.AuthenticationResponse;
import com.financialtools.model.User;
import com.financialtools.security.JwtUtil;
import com.financialtools.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public void register(@RequestBody User user) {
        userService.registerUser(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        User validatedUser = userService.validateUser(user.getEmail(), user.getPassword());
        if (validatedUser != null) {
            String jwt = jwtUtil.generateToken(validatedUser.getEmail());
            return ResponseEntity.ok(new AuthenticationResponse(jwt));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }
}
