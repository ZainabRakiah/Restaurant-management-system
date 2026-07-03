package com.restaurant.common.dto;

public record ApiError(
        String message,
        int status
) {
}
