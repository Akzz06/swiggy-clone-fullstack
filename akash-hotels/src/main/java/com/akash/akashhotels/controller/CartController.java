package com.akash.akashhotels.controller;

import com.akash.akashhotels.dto.CartItemRequest;
import com.akash.akashhotels.dto.CartResponse;
import com.akash.akashhotels.entity.User;
import com.akash.akashhotels.service.AuthService;
import com.akash.akashhotels.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final AuthService authService;

    public CartController(CartService cartService, AuthService authService) {
        this.cartService = cartService;
        this.authService = authService;
    }

    @GetMapping
    public ResponseEntity<CartResponse> getCart() {
        User user = authService.getCurrentUser();
        return ResponseEntity.ok(cartService.getCartResponse(user));
    }

    @PostMapping("/items")
    public ResponseEntity<CartResponse> addItemToCart(@Valid @RequestBody CartItemRequest request) {
        User user = authService.getCurrentUser();
        return ResponseEntity.ok(cartService.addItemToCart(user, request));
    }

    @PutMapping("/items/{id}")
    public ResponseEntity<CartResponse> updateCartItem(
            @PathVariable Long id,
            @RequestParam int quantity) {
        User user = authService.getCurrentUser();
        return ResponseEntity.ok(cartService.updateCartItem(user, id, quantity));
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<CartResponse> removeCartItem(@PathVariable Long id) {
        User user = authService.getCurrentUser();
        return ResponseEntity.ok(cartService.removeCartItem(user, id));
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart() {
        User user = authService.getCurrentUser();
        cartService.clearCart(user);
        return ResponseEntity.noContent().build();
    }
}
