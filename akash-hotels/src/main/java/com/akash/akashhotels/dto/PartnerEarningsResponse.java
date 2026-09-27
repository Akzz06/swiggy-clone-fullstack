package com.akash.akashhotels.dto;

import java.util.ArrayList;
import java.util.List;

public class PartnerEarningsResponse {

    private Long deliveryPartnerId;
    private String deliveryPartnerName;
    private int totalDeliveries;
    private Double totalBaseEarning;
    private Double totalBonusEarning;
    private Double totalEarning;
    private List<DeliveryResponse> deliveries = new ArrayList<>();

    public PartnerEarningsResponse() {
    }

    public PartnerEarningsResponse(Long deliveryPartnerId, String deliveryPartnerName, int totalDeliveries,
                                   Double totalBaseEarning, Double totalBonusEarning, Double totalEarning,
                                   List<DeliveryResponse> deliveries) {
        this.deliveryPartnerId = deliveryPartnerId;
        this.deliveryPartnerName = deliveryPartnerName;
        this.totalDeliveries = totalDeliveries;
        this.totalBaseEarning = totalBaseEarning;
        this.totalBonusEarning = totalBonusEarning;
        this.totalEarning = totalEarning;
        this.deliveries = deliveries;
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

    public int getTotalDeliveries() {
        return totalDeliveries;
    }

    public void setTotalDeliveries(int totalDeliveries) {
        this.totalDeliveries = totalDeliveries;
    }

    public Double getTotalBaseEarning() {
        return totalBaseEarning;
    }

    public void setTotalBaseEarning(Double totalBaseEarning) {
        this.totalBaseEarning = totalBaseEarning;
    }

    public Double getTotalBonusEarning() {
        return totalBonusEarning;
    }

    public void setTotalBonusEarning(Double totalBonusEarning) {
        this.totalBonusEarning = totalBonusEarning;
    }

    public Double getTotalEarning() {
        return totalEarning;
    }

    public void setTotalEarning(Double totalEarning) {
        this.totalEarning = totalEarning;
    }

    public List<DeliveryResponse> getDeliveries() {
        return deliveries;
    }

    public void setDeliveries(List<DeliveryResponse> deliveries) {
        this.deliveries = deliveries;
    }
}
