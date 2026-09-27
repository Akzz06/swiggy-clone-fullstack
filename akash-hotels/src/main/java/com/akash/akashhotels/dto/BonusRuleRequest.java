package com.akash.akashhotels.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class BonusRuleRequest {

    @NotNull(message = "Max minutes is required")
    @Min(value = 1, message = "Max minutes must be at least 1")
    private Integer maxMinutes;

    @NotNull(message = "Bonus amount is required")
    @DecimalMin(value = "0.0", message = "Bonus amount must be non-negative")
    private Double bonusAmount;

    private Boolean active = true;

    public BonusRuleRequest() {
    }

    public BonusRuleRequest(Integer maxMinutes, Double bonusAmount, Boolean active) {
        this.maxMinutes = maxMinutes;
        this.bonusAmount = bonusAmount;
        this.active = active;
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
