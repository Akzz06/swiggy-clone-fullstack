package com.akash.akashhotels.dto;

import jakarta.validation.constraints.NotNull;

public class DeliveryPartnerRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    private String vehicleType;
    private String vehicleNumber;
    private Boolean active = true;

    public DeliveryPartnerRequest() {
    }

    public DeliveryPartnerRequest(Long userId, String vehicleType, String vehicleNumber, Boolean active) {
        this.userId = userId;
        this.vehicleType = vehicleType;
        this.vehicleNumber = vehicleNumber;
        this.active = active;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
