package com.akash.akashhotels.service;

import com.akash.akashhotels.dto.DishRequest;
import com.akash.akashhotels.dto.DishResponse;
import com.akash.akashhotels.entity.Dish;
import com.akash.akashhotels.entity.Hotel;
import com.akash.akashhotels.exception.ResourceNotFoundException;
import com.akash.akashhotels.repository.DishRepository;
import com.akash.akashhotels.repository.HotelRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishService {

    private final DishRepository dishRepository;
    private final HotelRepository hotelRepository;

    public DishService(DishRepository dishRepository, HotelRepository hotelRepository) {
        this.dishRepository = dishRepository;
        this.hotelRepository = hotelRepository;
    }

    public List<DishResponse> getAllDishes(Boolean availableOnly) {
        List<Dish> dishes = (availableOnly != null && availableOnly)
                ? dishRepository.findByAvailableTrue()
                : dishRepository.findAll();

        return dishes.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public DishResponse getDishById(Long id) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dish not found with id: " + id));
        return mapToResponse(dish);
    }

    public DishResponse createDish(DishRequest request) {
        Hotel hotel;
        if (request.getHotelId() != null) {
            hotel = hotelRepository.findById(request.getHotelId())
                    .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + request.getHotelId()));
        } else {
            // Default to first hotel (Akash Hotels Guindy)
            hotel = hotelRepository.findAll().stream().findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("No hotel found. Please create a hotel first."));
        }

        Dish dish = new Dish();
        dish.setHotel(hotel);
        dish.setName(request.getName());
        dish.setDescription(request.getDescription());
        dish.setPrice(request.getPrice());
        dish.setImageUrl(request.getImageUrl());
        dish.setCategory(request.getCategory());
        dish.setAvailable(request.getAvailable() != null ? request.getAvailable() : true);

        return mapToResponse(dishRepository.save(dish));
    }

    public DishResponse updateDish(Long id, DishRequest request) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dish not found with id: " + id));

        if (request.getHotelId() != null) {
            Hotel hotel = hotelRepository.findById(request.getHotelId())
                    .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + request.getHotelId()));
            dish.setHotel(hotel);
        }

        dish.setName(request.getName());
        dish.setDescription(request.getDescription());
        dish.setPrice(request.getPrice());
        dish.setImageUrl(request.getImageUrl());
        dish.setCategory(request.getCategory());
        if (request.getAvailable() != null) {
            dish.setAvailable(request.getAvailable());
        }

        return mapToResponse(dishRepository.save(dish));
    }

    public void deleteDish(Long id) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dish not found with id: " + id));
        dish.setAvailable(false);
        dishRepository.save(dish);
    }

    public DishResponse mapToResponse(Dish dish) {
        return new DishResponse(
                dish.getId(),
                dish.getHotel() != null ? dish.getHotel().getId() : null,
                dish.getHotel() != null ? dish.getHotel().getName() : null,
                dish.getName(),
                dish.getDescription(),
                dish.getPrice(),
                dish.getImageUrl(),
                dish.getCategory(),
                dish.getAvailable(),
                dish.getCreatedAt(),
                dish.getUpdatedAt()
        );
    }
}
