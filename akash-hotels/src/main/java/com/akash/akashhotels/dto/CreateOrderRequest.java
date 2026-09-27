package com.akash.akashhotels.dto;

import com.akash.akashhotels.entity.PaymentMethod;
import jakarta.validation.constraints.NotNull;

public class CreateOrderRequest {

    @NotNull(message = "Delivery location ID is required")
    private Long deliveryLocationId;

    private PaymentMethod paymentMethod = PaymentMethod.MOCK_PAYMENT;

    private String addressDetails;

    public CreateOrderRequest() {
    }

    public CreateOrderRequest(Long deliveryLocationId, PaymentMethod paymentMethod, String addressDetails) {
        this.deliveryLocationId = deliveryLocationId;
        this.paymentMethod = paymentMethod;
        this.addressDetails = addressDetails;
    }

    public Long getDeliveryLocationId() {
        return deliveryLocationId;
    }

    public void setDeliveryLocationId(Long deliveryLocationId) {
        this.deliveryLocationId = deliveryLocationId;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getAddressDetails() {
        return addressDetails;
    }

    public void setAddressDetails(String addressDetails) {
        this.addressDetails = addressDetails;
    }
}
