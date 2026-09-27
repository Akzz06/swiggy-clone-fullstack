package com.akash.akashhotels.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "delivery_distance_rules")
public class DeliveryDistanceRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "min_distance_km", nullable = false)
    private Double minDistanceKm;

    @Column(name = "max_distance_km", nullable = false)
    private Double maxDistanceKm;

    @Column(name = "base_amount", nullable = false)
    private Double baseAmount;

    private Boolean active = true;

    public DeliveryDistanceRule() {
    }

    public DeliveryDistanceRule(Long id, Double minDistanceKm, Double maxDistanceKm, Double baseAmount, Boolean active) {
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
