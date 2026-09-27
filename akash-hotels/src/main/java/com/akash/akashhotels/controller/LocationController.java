package com.akash.akashhotels.controller;

import com.akash.akashhotels.dto.LocationRequest;
import com.akash.akashhotels.dto.LocationResponse;
import com.akash.akashhotels.service.LocationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("/api/locations")
    public ResponseEntity<List<LocationResponse>> getAllLocations(
            @RequestParam(required = false, defaultValue = "true") boolean activeOnly) {
        return ResponseEntity.ok(locationService.getAllLocations(activeOnly));
    }

    @GetMapping("/api/locations/{id}")
    public ResponseEntity<LocationResponse> getLocationById(@PathVariable Long id) {
        return ResponseEntity.ok(locationService.getLocationById(id));
    }

    @PostMapping("/api/admin/locations")
    @PreAuthorize("hasRole('HOTEL_ADMIN')")
    public ResponseEntity<LocationResponse> createLocation(@Valid @RequestBody LocationRequest request) {
        return new ResponseEntity<>(locationService.createLocation(request), HttpStatus.CREATED);
    }

    @PutMapping("/api/admin/locations/{id}")
    @PreAuthorize("hasRole('HOTEL_ADMIN')")
    public ResponseEntity<LocationResponse> updateLocation(@PathVariable Long id, @Valid @RequestBody LocationRequest request) {
        return ResponseEntity.ok(locationService.updateLocation(id, request));
    }

    @DeleteMapping("/api/admin/locations/{id}")
    @PreAuthorize("hasRole('HOTEL_ADMIN')")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id) {
        locationService.deleteLocation(id);
        return ResponseEntity.noContent().build();
    }
}
