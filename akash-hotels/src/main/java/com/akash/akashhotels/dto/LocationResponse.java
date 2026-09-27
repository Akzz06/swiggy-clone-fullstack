package com.akash.akashhotels.dto;

public class LocationResponse {

    private Long id;
    private String name;
    private String city;
    private Double distanceFromHotelKm;
    private Boolean active;

    public LocationResponse() {
    }

    public LocationResponse(Long id, String name, String city, Double distanceFromHotelKm, Boolean active) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.distanceFromHotelKm = distanceFromHotelKm;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
