package com.akash.akashhotels.dto;

public class OrderItemResponse {

    private Long id;
    private Long dishId;
    private String dishName;
    private Integer quantity;
    private Double unitPrice;
    private Double subtotal;

    public OrderItemResponse() {
    }

    public OrderItemResponse(Long id, Long dishId, String dishName, Integer quantity, Double unitPrice, Double subtotal) {
        this.id = id;
        this.dishId = dishId;
        this.dishName = dishName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subtotal = subtotal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDishId() {
        return dishId;
    }

    public void setDishId(Long dishId) {
        this.dishId = dishId;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }
}
