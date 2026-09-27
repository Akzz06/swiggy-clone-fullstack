package com.akash.akashhotels.controller;

import com.akash.akashhotels.dto.BonusRuleRequest;
import com.akash.akashhotels.dto.BonusRuleResponse;
import com.akash.akashhotels.dto.DistanceRuleRequest;
import com.akash.akashhotels.dto.DistanceRuleResponse;
import com.akash.akashhotels.service.DeliveryRuleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/delivery-rules")
@PreAuthorize("hasRole('HOTEL_ADMIN')")
public class AdminDeliveryRuleController {

    private final DeliveryRuleService ruleService;

    public AdminDeliveryRuleController(DeliveryRuleService ruleService) {
        this.ruleService = ruleService;
    }

    @GetMapping("/distance")
    public ResponseEntity<List<DistanceRuleResponse>> getDistanceRules() {
        return ResponseEntity.ok(ruleService.getDistanceRules());
    }

    @PostMapping("/distance")
    public ResponseEntity<DistanceRuleResponse> createDistanceRule(@Valid @RequestBody DistanceRuleRequest request) {
        return new ResponseEntity<>(ruleService.createDistanceRule(request), HttpStatus.CREATED);
    }

    @PutMapping("/distance/{id}")
    public ResponseEntity<DistanceRuleResponse> updateDistanceRule(
            @PathVariable Long id,
            @Valid @RequestBody DistanceRuleRequest request) {
        return ResponseEntity.ok(ruleService.updateDistanceRule(id, request));
    }

    @GetMapping("/bonus")
    public ResponseEntity<List<BonusRuleResponse>> getBonusRules() {
        return ResponseEntity.ok(ruleService.getBonusRules());
    }

    @PostMapping("/bonus")
    public ResponseEntity<BonusRuleResponse> createBonusRule(@Valid @RequestBody BonusRuleRequest request) {
        return new ResponseEntity<>(ruleService.createBonusRule(request), HttpStatus.CREATED);
    }

    @PutMapping("/bonus/{id}")
    public ResponseEntity<BonusRuleResponse> updateBonusRule(
            @PathVariable Long id,
            @Valid @RequestBody BonusRuleRequest request) {
        return ResponseEntity.ok(ruleService.updateBonusRule(id, request));
    }
}
