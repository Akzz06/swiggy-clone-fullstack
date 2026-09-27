package com.akash.akashhotels.controller;

import com.akash.akashhotels.dto.CreateOrderRequest;
import com.akash.akashhotels.dto.OrderResponse;
import com.akash.akashhotels.entity.User;
import com.akash.akashhotels.service.AuthService;
import com.akash.akashhotels.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final AuthService authService;

    public OrderController(OrderService orderService, AuthService authService) {
        this.orderService = orderService;
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(@Valid @RequestBody CreateOrderRequest request) {
        User user = authService.getCurrentUser();
        return new ResponseEntity<>(orderService.placeOrder(user, request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getCustomerOrders() {
        User user = authService.getCurrentUser();
        return ResponseEntity.ok(orderService.getCustomerOrders(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable Long id) {
        User user = authService.getCurrentUser();
        return ResponseEntity.ok(orderService.cancelOrder(id, user));
    }
}
