package com.example.exchange_rate_chal.service;

import com.example.exchange_rate_chal.model.dto.AuthResponse;
import com.example.exchange_rate_chal.model.dto.LoginRequest;
import com.example.exchange_rate_chal.model.dto.RegisterRequest;
import com.example.exchange_rate_chal.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    private final ConcurrentHashMap<String, String> users = new ConcurrentHashMap<>();

    public String register(RegisterRequest request) {
        if (users.containsKey(request.getUsername())) {
            throw new RuntimeException("Username already in use");
        }

        users.put(request.getUsername(), passwordEncoder.encode(request.getPassword()));

        return "Account created successfully. Please log in to get your token.";
    }

    public AuthResponse login(LoginRequest request) {
        String encodedPassword = users.get(request.getUsername());

        if (encodedPassword == null ||
                !passwordEncoder.matches(request.getPassword(), encodedPassword)) {
            throw new RuntimeException("Invalid username or password");
        }

        return new AuthResponse(jwtUtil.generateToken(request.getUsername()));
    }
}
