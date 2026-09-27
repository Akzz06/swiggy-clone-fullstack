package com.akash.akashhotels.dto;

public class DistanceRuleResponse {

    private Long id;
    private Double minDistanceKm;
    private Double maxDistanceKm;
    private Double baseAmount;
    private Boolean active;

    public DistanceRuleResponse() {
    }

    public DistanceRuleResponse(Long id, Double minDistanceKm, Double maxDistanceKm, Double baseAmount, Boolean active) {
        this.id = id;
        this.minDistanceKm = minDistanceKm;
        this.maxDistanceKm = maxDistanceKm;
        this.baseAmount = baseAmount;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
