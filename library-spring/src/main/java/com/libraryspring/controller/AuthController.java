package com.libraryspring.controller;

import com.libraryspring.dto.UserDTO;
import com.libraryspring.entity.User;
import com.libraryspring.service.UserService;
import com.libraryspring.utils.PasswordUtils;
import com.libraryspring.utils.TokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RestController
@RequestMapping
@CrossOrigin(origins = "http://localhost:8081/*")
@RequiredArgsConstructor
public class AuthController {
    @Autowired
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");
        String type_auth = "local";
        System.out.println(email + " " + password);
        User user = userService.loadUserLogin(email, type_auth);
        Map<String, String> response = new HashMap<>();
        if (PasswordUtils.verifyPassword(password, user.getHash())) {
            String accessToken = TokenUtils.generateAccessToken(user.getId(), String.valueOf(user.getRole()));
            String refreshToken = TokenUtils.generateRefreshToken(user.getId(), String.valueOf(user.getRole()));
            response.put("AccessToken", accessToken);
            response.put("RefreshToken", refreshToken);
            System.out.println(response);
        } else {
            response.put("error", "Невірний логін або пароль");
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/login/auth0")
    public ResponseEntity<?> authLogin(@RequestBody Map<String, String> body) {
        String token = body.get("token");
        Map<String, String> response = new HashMap<>();
        if (token == null) {
            response.put("message", "Token is null");
            return ResponseEntity.badRequest().body(response);
        }
        String email = TokenUtils.getValidToken(token);
        if (email == null) {
            response.put("message", "Email not found");
            return ResponseEntity.badRequest().body(response);
        }
        User user = userService.loadUserLogin(email, "auth0");
        if (user != null) {
            String access = TokenUtils.generateAccessToken(user.getId(), String.valueOf(user.getRole()));
            String refresh = TokenUtils.generateRefreshToken(user.getId(), String.valueOf(user.getRole()));
            response.put("AccessToken", access);
            response.put("RefreshToken", refresh);
        } else {
            response.put("message", "User not found");
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody UserDTO userDTO) {
        if (userService.addUser(userDTO)) {
            return ResponseEntity.ok("add successful");
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int userId = Integer.parseInt(authentication.getName());
        String role = authentication.getAuthorities().iterator().next().getAuthority().substring(5);
        if (role != null) {
            Map<String, String> tokenMap = new HashMap<>();
            String accessToken = TokenUtils.generateAccessToken(userId, role);
            String refreshToken = TokenUtils.generateRefreshToken(userId, role);
            tokenMap.put("AccessToken", accessToken);
            tokenMap.put("RefreshToken", refreshToken);
            System.out.println(tokenMap);
            return ResponseEntity.ok(tokenMap);
        }
        return ResponseEntity.badRequest().build();
    }
}
