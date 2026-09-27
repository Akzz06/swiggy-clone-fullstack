package com.akash.akashhotels.repository;

import com.akash.akashhotels.entity.Order;
import com.akash.akashhotels.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomerIdOrderByPlacedAtDesc(Long customerId);
    List<Order> findByHotelIdOrderByPlacedAtDesc(Long hotelId);
    List<Order> findByOrderStatus(OrderStatus orderStatus);
    List<Order> findByDeliveryPartnerId(Long deliveryPartnerId);
    List<Order> findAllByOrderByPlacedAtDesc();
}
