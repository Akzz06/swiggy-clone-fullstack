package com.akash.akashhotels.controller;

import com.akash.akashhotels.dto.AssignPartnerRequest;
import com.akash.akashhotels.dto.OrderResponse;
import com.akash.akashhotels.dto.OrderStatusUpdateRequest;
import com.akash.akashhotels.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
@PreAuthorize("hasRole('HOTEL_ADMIN')")
public class AdminOrderController {

    private final OrderService orderService;

    public AdminOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable Long id,
            @Valid @RequestBody OrderStatusUpdateRequest request) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, request.getStatus()));
    }

    @PostMapping("/{id}/assign")
    public ResponseEntity<OrderResponse> assignDeliveryPartner(
            @PathVariable Long id,
            @Valid @RequestBody AssignPartnerRequest request) {
        return ResponseEntity.ok(orderService.assignDeliveryPartner(id, request.getDeliveryPartnerId()));
    }
}
