package com.example.controller;

import com.example.security.auth.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8080")
public class AuthenticantionController {

    private final AuthenticationService service;


    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request

    ) {
        return ResponseEntity.ok(service.register(request));

    }
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request

    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }

//    @PostMapping("/api/login")
//    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
//        // Sprawdź poprawność danych logowania
//        if (service.authenticate(loginRequest.getEmail(), loginRequest.getPassword())) {
//            // Generuj token JWT
//            String token = generateToken(loginRequest.getUsername());
//            return ResponseEntity.ok(new LoginResponse(token));
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Nieprawidłowe dane logowania.");
//        }
//    }



}
