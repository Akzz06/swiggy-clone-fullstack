package com.akash.akashhotels.dto;

import com.akash.akashhotels.entity.DeliveryStatus;
import java.time.LocalDateTime;

public class DeliveryResponse {

    private Long id;
    private Long orderId;
    private Long deliveryPartnerId;
    private String deliveryPartnerName;
    private LocalDateTime pickupTime;
    private LocalDateTime deliveryStartTime;
    private LocalDateTime deliveredTime;
    private Double distanceKm;
    private Double baseEarning;
    private Double bonusAmount;
    private Double totalEarning;
    private DeliveryStatus deliveryStatus;

    public DeliveryResponse() {
    }

    public DeliveryResponse(Long id, Long orderId, Long deliveryPartnerId, String deliveryPartnerName,
                            LocalDateTime pickupTime, LocalDateTime deliveryStartTime, LocalDateTime deliveredTime,
                            Double distanceKm, Double baseEarning, Double bonusAmount, Double totalEarning,
                            DeliveryStatus deliveryStatus) {
        this.id = id;
        this.orderId = orderId;
        this.deliveryPartnerId = deliveryPartnerId;
        this.deliveryPartnerName = deliveryPartnerName;
        this.pickupTime = pickupTime;
        this.deliveryStartTime = deliveryStartTime;
        this.deliveredTime = deliveredTime;
        this.distanceKm = distanceKm;
        this.baseEarning = baseEarning;
        this.bonusAmount = bonusAmount;
        this.totalEarning = totalEarning;
        this.deliveryStatus = deliveryStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getDeliveryPartnerId() {
        return deliveryPartnerId;
    }

    public void setDeliveryPartnerId(Long deliveryPartnerId) {
        this.deliveryPartnerId = deliveryPartnerId;
    }

    public String getDeliveryPartnerName() {
        return deliveryPartnerName;
    }

    public void setDeliveryPartnerName(String deliveryPartnerName) {
        this.deliveryPartnerName = deliveryPartnerName;
    }

    public LocalDateTime getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(LocalDateTime pickupTime) {
        this.pickupTime = pickupTime;
    }

    public LocalDateTime getDeliveryStartTime() {
        return deliveryStartTime;
    }

    public void setDeliveryStartTime(LocalDateTime deliveryStartTime) {
        this.deliveryStartTime = deliveryStartTime;
    }

    public LocalDateTime getDeliveredTime() {
        return deliveredTime;
    }

    public void setDeliveredTime(LocalDateTime deliveredTime) {
        this.deliveredTime = deliveredTime;
    }

    public Double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(Double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public Double getBaseEarning() {
        return baseEarning;
    }

    public void setBaseEarning(Double baseEarning) {
        this.baseEarning = baseEarning;
    }

    public Double getBonusAmount() {
        return bonusAmount;
    }

    public void setBonusAmount(Double bonusAmount) {
        this.bonusAmount = bonusAmount;
    }

    public Double getTotalEarning() {
        return totalEarning;
    }

    public void setTotalEarning(Double totalEarning) {
        this.totalEarning = totalEarning;
    }

    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }
}
