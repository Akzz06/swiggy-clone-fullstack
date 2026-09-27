package com.akash.akashhotels.repository;

import com.akash.akashhotels.entity.DeliveryDistanceRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryDistanceRuleRepository extends JpaRepository<DeliveryDistanceRule, Long> {
    List<DeliveryDistanceRule> findByActiveTrueOrderByMinDistanceKmAsc();
}
