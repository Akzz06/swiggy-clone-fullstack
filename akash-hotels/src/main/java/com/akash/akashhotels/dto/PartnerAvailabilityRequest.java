package com.akash.akashhotels.dto;

import com.akash.akashhotels.entity.AvailabilityStatus;
import jakarta.validation.constraints.NotNull;

public class PartnerAvailabilityRequest {

    @NotNull(message = "Availability status is required")
    private AvailabilityStatus availabilityStatus;

    public PartnerAvailabilityRequest() {
    }

    public PartnerAvailabilityRequest(AvailabilityStatus availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    public AvailabilityStatus getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(AvailabilityStatus availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }
}
