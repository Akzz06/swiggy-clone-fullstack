package com.akash.akashhotels.dto;

public class BonusRuleResponse {

    private Long id;
    private Integer maxMinutes;
    private Double bonusAmount;
    private Boolean active;

    public BonusRuleResponse() {
    }

    public BonusRuleResponse(Long id, Integer maxMinutes, Double bonusAmount, Boolean active) {
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
