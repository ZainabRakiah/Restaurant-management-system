package com.restaurant.common.dto;

import java.time.LocalDateTime;
import java.util.List;

public record OrderItemResponse(
        Long menuItemId,
        String itemName,
        Integer quantity,
        Double unitPrice,
        Double lineTotal
) {
}
