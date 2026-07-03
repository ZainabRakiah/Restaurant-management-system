package com.restaurant.common.dto;

import jakarta.validation.constraints.NotNull;

public record DeliveryRequest(
        @NotNull Long orderId,
        @NotNull Long driverId,
        String notes
) {
}
