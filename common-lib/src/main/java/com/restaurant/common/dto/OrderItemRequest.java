package com.restaurant.common.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrderItemRequest(
        @NotNull Long menuItemId,
        @NotNull Integer quantity
) {
}
