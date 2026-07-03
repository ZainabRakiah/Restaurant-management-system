package com.restaurant.common.dto;

public record RestaurantResponse(
        Long id,
        String name,
        String cuisine,
        String address,
        Double rating,
        boolean active
) {
}
