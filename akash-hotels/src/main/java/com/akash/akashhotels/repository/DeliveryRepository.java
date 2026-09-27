package com.akash.akashhotels.repository;

import com.akash.akashhotels.entity.Delivery;
import com.akash.akashhotels.entity.DeliveryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    Optional<Delivery> findByOrderId(Long orderId);
    List<Delivery> findByDeliveryPartnerId(Long deliveryPartnerId);
    List<Delivery> findByDeliveryPartnerIdAndDeliveryStatusIn(Long deliveryPartnerId, List<DeliveryStatus> statuses);
}
