package com.restaurant.common.dto;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        Long customerId,
        Long restaurantId,
        String status,
        Double totalAmount,
        String deliveryAddress,
        List<OrderItemResponse> items,
        LocalDateTime createdAt
) {
}
