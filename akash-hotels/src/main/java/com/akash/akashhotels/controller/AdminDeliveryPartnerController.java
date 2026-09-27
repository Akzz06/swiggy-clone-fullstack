package com.akash.akashhotels.controller;

import com.akash.akashhotels.dto.DeliveryPartnerRequest;
import com.akash.akashhotels.dto.DeliveryPartnerResponse;
import com.akash.akashhotels.service.DeliveryPartnerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/delivery-partners")
@PreAuthorize("hasRole('HOTEL_ADMIN')")
public class AdminDeliveryPartnerController {

    private final DeliveryPartnerService partnerService;

    public AdminDeliveryPartnerController(DeliveryPartnerService partnerService) {
        this.partnerService = partnerService;
    }

    @GetMapping
    public ResponseEntity<List<DeliveryPartnerResponse>> getAllPartners() {
        return ResponseEntity.ok(partnerService.getAllPartners());
    }

    @GetMapping("/available")
    public ResponseEntity<List<DeliveryPartnerResponse>> getAvailablePartners() {
        return ResponseEntity.ok(partnerService.getAvailablePartners());
    }

    @PostMapping
    public ResponseEntity<DeliveryPartnerResponse> createPartner(@Valid @RequestBody DeliveryPartnerRequest request) {
        return new ResponseEntity<>(partnerService.createPartner(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeliveryPartnerResponse> updatePartner(
            @PathVariable Long id,
            @Valid @RequestBody DeliveryPartnerRequest request) {
        return ResponseEntity.ok(partnerService.updatePartner(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePartner(@PathVariable Long id) {
        partnerService.deletePartner(id);
        return ResponseEntity.noContent().build();
    }
}
