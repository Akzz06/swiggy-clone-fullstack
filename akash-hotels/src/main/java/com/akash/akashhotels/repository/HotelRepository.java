package com.akash.akashhotels.repository;

import com.akash.akashhotels.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
}