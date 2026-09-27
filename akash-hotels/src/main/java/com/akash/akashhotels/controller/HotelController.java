package com.akash.akashhotels.controller;

import com.akash.akashhotels.entity.Hotel;
import com.akash.akashhotels.service.HotelService;
import org.springframework.web.bind.annotation.*;
import com.akash.akashhotels.dto.HotelRequest;
import java.util.List;

@RestController
@RequestMapping("/api/hotels")
public class HotelController {

    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @PostMapping
    public Hotel createHotel(@RequestBody HotelRequest request) {
            return hotelService.createHotel(request);
    }

    @GetMapping
    public List<Hotel> getAllHotels() {
        return hotelService.getAllHotels();
    }

    @GetMapping("/{id}")
    public Hotel getHotelById(@PathVariable Long id) {
        return hotelService.getHotelById(id);
    }
}