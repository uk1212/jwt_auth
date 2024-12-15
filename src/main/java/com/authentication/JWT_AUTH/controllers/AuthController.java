package com.authentication.JWT_AUTH.controllers;
import com.authentication.JWT_AUTH.dto.AuthRequest;
import com.authentication.JWT_AUTH.dto.AuthResponse;
import com.authentication.JWT_AUTH.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("api/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request, HttpServletResponse response) {
        System.out.println("Hello");
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        String accessToken = jwtUtil.generateAccessToken(request.getUsername());
        String refreshToken = jwtUtil.generateRefreshToken(request.getUsername()); // You can create separate refresh token logic
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("auth/api/refresh");
        response.addCookie(cookie);

        return ResponseEntity.ok(new AuthResponse(accessToken));
    }

    @PostMapping("api/refresh")
    public ResponseEntity<?> refreshToken(@CookieValue("refreshToken") String refreshToken, @RequestBody AuthRequest request, HttpServletResponse response ){
        if (jwtUtil.validateToken(refreshToken)) {
            String accessToken = jwtUtil.generateAccessToken(request.getUsername());
            String updatedRefreshToken = jwtUtil.generateRefreshToken(request.getUsername()); // You can create separate refresh token logic
            Cookie updatedCookie = new Cookie("refreshToken", updatedRefreshToken);
            updatedCookie.setHttpOnly(true);
            updatedCookie.setSecure(true);
            updatedCookie.setPath("auth/api/refresh");
            response.addCookie(updatedCookie);
            return ResponseEntity.ok(new AuthResponse(accessToken));
        }
        return ResponseEntity.status(401).body("Invalid refresh token");
    }
}

