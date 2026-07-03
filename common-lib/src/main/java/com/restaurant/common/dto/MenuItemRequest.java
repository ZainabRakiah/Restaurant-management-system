package com.restaurant.common.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record MenuItemRequest(
        @NotNull Long restaurantId,
        @NotBlank String name,
        String description,
        @NotNull @Positive Double price,
        String category
) {
}
