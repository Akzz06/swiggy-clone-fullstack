package com.akash.akashhotels.controller;

import com.akash.akashhotels.dto.PaymentResponse;
import com.akash.akashhotels.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * GET /api/orders/{orderId}/payment
     * Customers can view the payment for their own order.
     * Hotel admin can view payment for any order.
     */
    @GetMapping("/api/orders/{orderId}/payment")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'HOTEL_ADMIN')")
    public ResponseEntity<PaymentResponse> getPaymentByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(paymentService.getPaymentByOrderId(orderId));
    }

    /**
     * GET /api/admin/payments
     * Admin-only: view all payments.
     */
    @GetMapping("/api/admin/payments")
    @PreAuthorize("hasRole('HOTEL_ADMIN')")
    public ResponseEntity<List<PaymentResponse>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }
}
