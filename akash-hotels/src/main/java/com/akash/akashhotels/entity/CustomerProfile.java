package com.akash.akashhotels.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "customer_profiles")
public class CustomerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "default_location_id")
    private DeliveryLocation defaultLocation;

    @Column(name = "address_details")
    private String addressDetails;

    public CustomerProfile() {
    }

    public CustomerProfile(Long id, User user, DeliveryLocation defaultLocation, String addressDetails) {
        this.id = id;
        this.user = user;
        this.defaultLocation = defaultLocation;
        this.addressDetails = addressDetails;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public DeliveryLocation getDefaultLocation() {
        return defaultLocation;
    }

    public void setDefaultLocation(DeliveryLocation defaultLocation) {
        this.defaultLocation = defaultLocation;
    }

    public String getAddressDetails() {
        return addressDetails;
    }

    public void setAddressDetails(String addressDetails) {
        this.addressDetails = addressDetails;
    }
}
