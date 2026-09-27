package com.akash.akashhotels.repository;

import com.akash.akashhotels.entity.AvailabilityStatus;
import com.akash.akashhotels.entity.DeliveryPartner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryPartnerRepository extends JpaRepository<DeliveryPartner, Long> {
    Optional<DeliveryPartner> findByUserId(Long userId);
    List<DeliveryPartner> findByAvailabilityStatusAndActiveTrue(AvailabilityStatus availabilityStatus);
    List<DeliveryPartner> findByActiveTrue();
}
