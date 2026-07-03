package com.restaurant.common.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrderRequest(
        @NotNull Long customerId,
        @NotNull Long restaurantId,
        @NotEmpty List<OrderItemRequest> items,
        String deliveryAddress
) {
}
