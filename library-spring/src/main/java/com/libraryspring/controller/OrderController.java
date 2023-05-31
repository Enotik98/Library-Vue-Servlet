package com.libraryspring.controller;

import com.libraryspring.dto.OrderDTO;
import com.libraryspring.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RestController
@RequestMapping("/order")
@CrossOrigin(origins="http://localhost:8081")
@RequiredArgsConstructor
public class OrderController {
    @Autowired
    private final OrderService orderService;

    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllOrder() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getOrder(@PathVariable Integer id) {
        return ResponseEntity.ok(orderService.getOrder(id));
    }
    @PostMapping("")
    public ResponseEntity<?> createOrder(@RequestBody OrderDTO orderDTO){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int userId = Integer.parseInt(authentication.getName());
        if (orderService.addOrder(orderDTO, userId)){
            return ResponseEntity.ok("add successful");
        }else {
            return ResponseEntity.badRequest().build();
        }
    }
    @PutMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateOrder(@RequestBody OrderDTO orderDTO){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int userId = Integer.parseInt(authentication.getName());
        if (orderService.updateOrder(orderDTO, userId)){
            return ResponseEntity.ok("update successful");
        }else {
            return ResponseEntity.badRequest().build();
        }
    }
    @DeleteMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteOrder(@RequestBody Map<String, String> body){
        int id = Integer.parseInt(body.get("id"));
        if (orderService.deleteOrder(id)){
            return ResponseEntity.ok("delete successful");
        }else {
            return ResponseEntity.badRequest().build();
        }
    }

}
