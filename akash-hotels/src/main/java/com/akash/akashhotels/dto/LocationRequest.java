package com.akash.akashhotels.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class LocationRequest {

    @NotBlank(message = "Location name is required")
    private String name;

    @NotBlank(message = "City is required")
    private String city;

    @NotNull(message = "Distance from hotel is required")
    @DecimalMin(value = "0.0", message = "Distance must be non-negative")
    private Double distanceFromHotelKm;

    private Boolean active = true;

    public LocationRequest() {
    }

    public LocationRequest(String name, String city, Double distanceFromHotelKm, Boolean active) {
        this.name = name;
        this.city = city;
        this.distanceFromHotelKm = distanceFromHotelKm;
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Double getDistanceFromHotelKm() {
        return distanceFromHotelKm;
    }

    public void setDistanceFromHotelKm(Double distanceFromHotelKm) {
        this.distanceFromHotelKm = distanceFromHotelKm;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
