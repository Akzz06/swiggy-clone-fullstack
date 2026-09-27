package com.akash.akashhotels.dto;

import java.util.ArrayList;
import java.util.List;

public class CartResponse {

    private Long id;
    private Long customerId;
    private List<CartItemResponse> items = new ArrayList<>();
    private Double totalAmount = 0.0;

    public CartResponse() {
    }

    public CartResponse(Long id, Long customerId, List<CartItemResponse> items, Double totalAmount) {
        this.id = id;
        this.customerId = customerId;
        this.items = items;
        this.totalAmount = totalAmount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public List<CartItemResponse> getItems() {
        return items;
    }

    public void setItems(List<CartItemResponse> items) {
        this.items = items;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
