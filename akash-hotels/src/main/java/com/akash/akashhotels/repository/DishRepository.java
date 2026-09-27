package com.akash.akashhotels.repository;

import com.akash.akashhotels.entity.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {
    List<Dish> findByAvailableTrue();
    List<Dish> findByHotelId(Long hotelId);
    List<Dish> findByHotelIdAndAvailableTrue(Long hotelId);
    List<Dish> findByCategoryIgnoreCase(String category);
}
