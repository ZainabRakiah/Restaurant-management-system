package com.restaurant.common.dto;

import java.time.LocalDateTime;

public record DeliveryResponse(
        Long id,
        Long orderId,
        Long driverId,
        String status,
        String notes,
        LocalDateTime assignedAt,
        LocalDateTime deliveredAt
) {
}
