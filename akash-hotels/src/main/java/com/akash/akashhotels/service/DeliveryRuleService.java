package com.akash.akashhotels.service;

import com.akash.akashhotels.dto.BonusRuleRequest;
import com.akash.akashhotels.dto.BonusRuleResponse;
import com.akash.akashhotels.dto.DistanceRuleRequest;
import com.akash.akashhotels.dto.DistanceRuleResponse;
import com.akash.akashhotels.entity.DeliveryBonusRule;
import com.akash.akashhotels.entity.DeliveryDistanceRule;
import com.akash.akashhotels.exception.ResourceNotFoundException;
import com.akash.akashhotels.repository.DeliveryBonusRuleRepository;
import com.akash.akashhotels.repository.DeliveryDistanceRuleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeliveryRuleService {

    private final DeliveryDistanceRuleRepository distanceRuleRepository;
    private final DeliveryBonusRuleRepository bonusRuleRepository;

    public DeliveryRuleService(DeliveryDistanceRuleRepository distanceRuleRepository,
                               DeliveryBonusRuleRepository bonusRuleRepository) {
        this.distanceRuleRepository = distanceRuleRepository;
        this.bonusRuleRepository = bonusRuleRepository;
    }

    public List<DistanceRuleResponse> getDistanceRules() {
        return distanceRuleRepository.findByActiveTrueOrderByMinDistanceKmAsc().stream()
                .map(this::mapDistanceResponse)
                .collect(Collectors.toList());
    }

    public DistanceRuleResponse createDistanceRule(DistanceRuleRequest request) {
        DeliveryDistanceRule rule = new DeliveryDistanceRule();
        rule.setMinDistanceKm(request.getMinDistanceKm());
        rule.setMaxDistanceKm(request.getMaxDistanceKm());
        rule.setBaseAmount(request.getBaseAmount());
        rule.setActive(request.getActive() != null ? request.getActive() : true);
        return mapDistanceResponse(distanceRuleRepository.save(rule));
    }

    public DistanceRuleResponse updateDistanceRule(Long id, DistanceRuleRequest request) {
        DeliveryDistanceRule rule = distanceRuleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Distance rule not found with id: " + id));

        rule.setMinDistanceKm(request.getMinDistanceKm());
        rule.setMaxDistanceKm(request.getMaxDistanceKm());
        rule.setBaseAmount(request.getBaseAmount());
        if (request.getActive() != null) {
            rule.setActive(request.getActive());
        }
        return mapDistanceResponse(distanceRuleRepository.save(rule));
    }

    public List<BonusRuleResponse> getBonusRules() {
        return bonusRuleRepository.findByActiveTrueOrderByMaxMinutesAsc().stream()
                .map(this::mapBonusResponse)
                .collect(Collectors.toList());
    }

    public BonusRuleResponse createBonusRule(BonusRuleRequest request) {
        DeliveryBonusRule rule = new DeliveryBonusRule();
        rule.setMaxMinutes(request.getMaxMinutes());
        rule.setBonusAmount(request.getBonusAmount());
        rule.setActive(request.getActive() != null ? request.getActive() : true);
        return mapBonusResponse(bonusRuleRepository.save(rule));
    }

    public BonusRuleResponse updateBonusRule(Long id, BonusRuleRequest request) {
        DeliveryBonusRule rule = bonusRuleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bonus rule not found with id: " + id));

        rule.setMaxMinutes(request.getMaxMinutes());
        rule.setBonusAmount(request.getBonusAmount());
        if (request.getActive() != null) {
            rule.setActive(request.getActive());
        }
        return mapBonusResponse(bonusRuleRepository.save(rule));
    }

    public Double calculateBaseEarning(Double distanceKm) {
        if (distanceKm == null) {
            return 30.0;
        }

        List<DeliveryDistanceRule> rules = distanceRuleRepository.findByActiveTrueOrderByMinDistanceKmAsc();
        for (DeliveryDistanceRule rule : rules) {
            if (distanceKm >= rule.getMinDistanceKm() && distanceKm < rule.getMaxDistanceKm()) {
                return rule.getBaseAmount();
            }
        }

        // If distance exceeds all rules or none match, use the last rule or fallback
        if (!rules.isEmpty()) {
            return rules.get(rules.size() - 1).getBaseAmount();
        }

        return 30.0;
    }

    public Double calculateBonusEarning(long durationMinutes) {
        List<DeliveryBonusRule> rules = bonusRuleRepository.findByActiveTrueOrderByMaxMinutesAsc();
        for (DeliveryBonusRule rule : rules) {
            if (durationMinutes <= rule.getMaxMinutes()) {
                return rule.getBonusAmount();
            }
        }
        return 0.0;
    }

    private DistanceRuleResponse mapDistanceResponse(DeliveryDistanceRule rule) {
        return new DistanceRuleResponse(
                rule.getId(),
                rule.getMinDistanceKm(),
                rule.getMaxDistanceKm(),
                rule.getBaseAmount(),
                rule.getActive()
        );
    }

    private BonusRuleResponse mapBonusResponse(DeliveryBonusRule rule) {
        return new BonusRuleResponse(
                rule.getId(),
                rule.getMaxMinutes(),
                rule.getBonusAmount(),
                rule.getActive()
        );
    }
}
