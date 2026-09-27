package com.akash.akashhotels.service;

import com.akash.akashhotels.dto.LocationRequest;
import com.akash.akashhotels.dto.LocationResponse;
import com.akash.akashhotels.entity.DeliveryLocation;
import com.akash.akashhotels.exception.ResourceNotFoundException;
import com.akash.akashhotels.repository.DeliveryLocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LocationService {

    private final DeliveryLocationRepository locationRepository;

    public LocationService(DeliveryLocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public List<LocationResponse> getAllLocations(boolean activeOnly) {
        List<DeliveryLocation> locations = activeOnly
                ? locationRepository.findByActiveTrue()
                : locationRepository.findAll();

        return locations.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public LocationResponse getLocationById(Long id) {
        DeliveryLocation location = locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location not found with id: " + id));
        return mapToResponse(location);
    }

    public LocationResponse createLocation(LocationRequest request) {
        DeliveryLocation location = new DeliveryLocation();
        location.setName(request.getName());
        location.setCity(request.getCity());
        location.setDistanceFromHotelKm(request.getDistanceFromHotelKm());
        location.setActive(request.getActive() != null ? request.getActive() : true);

        return mapToResponse(locationRepository.save(location));
    }

    public LocationResponse updateLocation(Long id, LocationRequest request) {
        DeliveryLocation location = locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location not found with id: " + id));

        location.setName(request.getName());
        location.setCity(request.getCity());
        location.setDistanceFromHotelKm(request.getDistanceFromHotelKm());
        if (request.getActive() != null) {
            location.setActive(request.getActive());
        }

        return mapToResponse(locationRepository.save(location));
    }

    public void deleteLocation(Long id) {
        DeliveryLocation location = locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location not found with id: " + id));
        location.setActive(false);
        locationRepository.save(location);
    }

    public LocationResponse mapToResponse(DeliveryLocation location) {
        return new LocationResponse(
                location.getId(),
                location.getName(),
                location.getCity(),
                location.getDistanceFromHotelKm(),
                location.getActive()
        );
    }
}
