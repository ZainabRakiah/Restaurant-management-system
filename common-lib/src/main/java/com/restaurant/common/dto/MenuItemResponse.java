package com.restaurant.common.dto;

public record MenuItemResponse(
        Long id,
        Long restaurantId,
        String name,
        String description,
        Double price,
        String category,
        boolean available
) {
}
