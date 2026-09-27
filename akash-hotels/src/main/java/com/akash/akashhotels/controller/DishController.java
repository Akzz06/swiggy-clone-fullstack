package com.akash.akashhotels.controller;

import com.akash.akashhotels.dto.DishRequest;
import com.akash.akashhotels.dto.DishResponse;
import com.akash.akashhotels.service.DishService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dishes")
public class DishController {

    private final DishService dishService;

    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    @GetMapping
    public ResponseEntity<List<DishResponse>> getAllDishes(
            @RequestParam(required = false, defaultValue = "false") Boolean availableOnly) {
        return ResponseEntity.ok(dishService.getAllDishes(availableOnly));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DishResponse> getDishById(@PathVariable Long id) {
        return ResponseEntity.ok(dishService.getDishById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('HOTEL_ADMIN')")
    public ResponseEntity<DishResponse> createDish(@Valid @RequestBody DishRequest request) {
        return new ResponseEntity<>(dishService.createDish(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('HOTEL_ADMIN')")
    public ResponseEntity<DishResponse> updateDish(@PathVariable Long id, @Valid @RequestBody DishRequest request) {
        return ResponseEntity.ok(dishService.updateDish(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('HOTEL_ADMIN')")
    public ResponseEntity<Void> deleteDish(@PathVariable Long id) {
        dishService.deleteDish(id);
        return ResponseEntity.noContent().build();
    }
}
