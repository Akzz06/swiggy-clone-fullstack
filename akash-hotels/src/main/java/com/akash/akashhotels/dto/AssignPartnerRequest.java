package com.akash.akashhotels.dto;

import jakarta.validation.constraints.NotNull;

public class AssignPartnerRequest {

    @NotNull(message = "Delivery partner ID is required")
    private Long deliveryPartnerId;

    public AssignPartnerRequest() {
    }

    public AssignPartnerRequest(Long deliveryPartnerId) {
        this.deliveryPartnerId = deliveryPartnerId;
    }

    public Long getDeliveryPartnerId() {
        return deliveryPartnerId;
    }

    public void setDeliveryPartnerId(Long deliveryPartnerId) {
        this.deliveryPartnerId = deliveryPartnerId;
    }
}
