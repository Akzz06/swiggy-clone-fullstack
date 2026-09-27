package com.akash.akashhotels.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "delivery_bonus_rules")
public class DeliveryBonusRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "max_minutes", nullable = false)
    private Integer maxMinutes;

    @Column(name = "bonus_amount", nullable = false)
    private Double bonusAmount;

    private Boolean active = true;

    public DeliveryBonusRule() {
    }

    public DeliveryBonusRule(Long id, Integer maxMinutes, Double bonusAmount, Boolean active) {
        this.id = id;
        this.maxMinutes = maxMinutes;
        this.bonusAmount = bonusAmount;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMaxMinutes() {
        return maxMinutes;
    }

    public void setMaxMinutes(Integer maxMinutes) {
        this.maxMinutes = maxMinutes;
    }

    public Double getBonusAmount() {
        return bonusAmount;
    }

    public void setBonusAmount(Double bonusAmount) {
        this.bonusAmount = bonusAmount;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
