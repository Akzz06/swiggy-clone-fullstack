package com.akash.akashhotels.controller;

import com.akash.akashhotels.dto.DeliveryPartnerResponse;
import com.akash.akashhotels.dto.DeliveryResponse;
import com.akash.akashhotels.dto.PartnerAvailabilityRequest;
import com.akash.akashhotels.dto.PartnerEarningsResponse;
import com.akash.akashhotels.entity.User;
import com.akash.akashhotels.service.AuthService;
import com.akash.akashhotels.service.DeliveryPartnerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/delivery-partners")
public class DeliveryPartnerController {

    private final DeliveryPartnerService partnerService;
    private final AuthService authService;

    public DeliveryPartnerController(DeliveryPartnerService partnerService, AuthService authService) {
        this.partnerService = partnerService;
        this.authService = authService;
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('DELIVERY_PARTNER')")
    public ResponseEntity<DeliveryPartnerResponse> getMyProfile() {
        User user = authService.getCurrentUser();
        return ResponseEntity.ok(partnerService.getPartnerByUserId(user.getId()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('DELIVERY_PARTNER', 'HOTEL_ADMIN')")
    public ResponseEntity<DeliveryPartnerResponse> getPartnerById(@PathVariable Long id) {
        return ResponseEntity.ok(partnerService.getPartnerById(id));
    }

    @PatchMapping("/{id}/availability")
    @PreAuthorize("hasAnyRole('DELIVERY_PARTNER', 'HOTEL_ADMIN')")
    public ResponseEntity<DeliveryPartnerResponse> updateAvailability(
            @PathVariable Long id,
            @Valid @RequestBody PartnerAvailabilityRequest request) {
        return ResponseEntity.ok(partnerService.updateAvailability(id, request.getAvailabilityStatus()));
    }

    @GetMapping("/{id}/deliveries")
    @PreAuthorize("hasAnyRole('DELIVERY_PARTNER', 'HOTEL_ADMIN')")
    public ResponseEntity<List<DeliveryResponse>> getDeliveries(@PathVariable Long id) {
        return ResponseEntity.ok(partnerService.getPartnerDeliveries(id));
    }

    @GetMapping("/{id}/earnings")
    @PreAuthorize("hasAnyRole('DELIVERY_PARTNER', 'HOTEL_ADMIN')")
    public ResponseEntity<PartnerEarningsResponse> getEarnings(@PathVariable Long id) {
        return ResponseEntity.ok(partnerService.getPartnerEarnings(id));
    }

    @PostMapping("/deliveries/{deliveryId}/accept")
    @PreAuthorize("hasAnyRole('DELIVERY_PARTNER', 'HOTEL_ADMIN')")
    public ResponseEntity<DeliveryResponse> acceptDelivery(@PathVariable Long deliveryId) {
        User user = authService.getCurrentUser();
        return ResponseEntity.ok(partnerService.acceptDelivery(deliveryId, user));
    }

    @PostMapping("/deliveries/{deliveryId}/pickup")
    @PreAuthorize("hasAnyRole('DELIVERY_PARTNER', 'HOTEL_ADMIN')")
    public ResponseEntity<DeliveryResponse> pickupDelivery(@PathVariable Long deliveryId) {
        User user = authService.getCurrentUser();
        return ResponseEntity.ok(partnerService.pickupDelivery(deliveryId, user));
    }

    @PostMapping("/deliveries/{deliveryId}/deliver")
    @PreAuthorize("hasAnyRole('DELIVERY_PARTNER', 'HOTEL_ADMIN')")
    public ResponseEntity<DeliveryResponse> completeDelivery(@PathVariable Long deliveryId) {
        User user = authService.getCurrentUser();
        return ResponseEntity.ok(partnerService.completeDelivery(deliveryId, user));
    }
}
