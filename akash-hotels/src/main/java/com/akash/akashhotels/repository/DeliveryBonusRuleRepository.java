package com.akash.akashhotels.repository;

import com.akash.akashhotels.entity.DeliveryBonusRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryBonusRuleRepository extends JpaRepository<DeliveryBonusRule, Long> {
    List<DeliveryBonusRule> findByActiveTrueOrderByMaxMinutesAsc();
}
