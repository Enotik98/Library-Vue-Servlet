package com.libraryspring.controller;


import com.libraryspring.dto.UserDTO;
import com.libraryspring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import lombok.RequiredArgsConstructor;


@Controller
@RestController
@RequestMapping("/user")
@CrossOrigin(origins="http://localhost:8081")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final UserService userService;

    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUser() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/info")
    public ResponseEntity<?> getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int userId = Integer.parseInt(authentication.getName());
        return ResponseEntity.ok(userService.getUser(userId));
    }
    @GetMapping("/order")
    public ResponseEntity<?> getUserOrder(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int userId = Integer.parseInt(authentication.getName());
        return ResponseEntity.ok(userService.getUserOrders(userId));
    }
    @PutMapping("")
    public ResponseEntity<?> updateUser(@RequestBody UserDTO userDTO){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int userId = Integer.parseInt(authentication.getName());
        if (userService.updateUser(userDTO, userId)){
            return ResponseEntity.ok("update successful");
        }
        return null;
    }
    @DeleteMapping("")
    public ResponseEntity<?> deleteUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int userId = Integer.parseInt(authentication.getName());
        if (userService.deleteUser(userId)){
            return ResponseEntity.ok("delete successful");
        }
        return null;
    }
}
