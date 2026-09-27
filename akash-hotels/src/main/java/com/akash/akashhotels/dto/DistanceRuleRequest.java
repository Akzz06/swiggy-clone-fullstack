package com.akash.akashhotels.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class DistanceRuleRequest {

    @NotNull(message = "Min distance is required")
    @DecimalMin(value = "0.0", message = "Min distance must be non-negative")
    private Double minDistanceKm;

    @NotNull(message = "Max distance is required")
    @DecimalMin(value = "0.0", message = "Max distance must be non-negative")
    private Double maxDistanceKm;

    @NotNull(message = "Base amount is required")
    @DecimalMin(value = "0.0", message = "Base amount must be non-negative")
    private Double baseAmount;

    private Boolean active = true;

    public DistanceRuleRequest() {
    }

    public DistanceRuleRequest(Double minDistanceKm, Double maxDistanceKm, Double baseAmount, Boolean active) {
        this.minDistanceKm = minDistanceKm;
        this.maxDistanceKm = maxDistanceKm;
        this.baseAmount = baseAmount;
        this.active = active;
    }

    public Double getMinDistanceKm() {
        return minDistanceKm;
    }

    public void setMinDistanceKm(Double minDistanceKm) {
        this.minDistanceKm = minDistanceKm;
    }

    public Double getMaxDistanceKm() {
        return maxDistanceKm;
    }

    public void setMaxDistanceKm(Double maxDistanceKm) {
        this.maxDistanceKm = maxDistanceKm;
    }

    public Double getBaseAmount() {
        return baseAmount;
    }

    public void setBaseAmount(Double baseAmount) {
        this.baseAmount = baseAmount;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
