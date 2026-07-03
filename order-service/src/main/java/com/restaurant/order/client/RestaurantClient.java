package com.restaurant.order.client;

import com.restaurant.common.dto.MenuItemResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Component
public class RestaurantClient {

    private final RestTemplate restTemplate;

    public RestaurantClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public MenuItemResponse getMenuItem(Long menuItemId) {
        try {
            return restTemplate.getForObject(
                    "http://restaurant-service/menu/{id}",
                    MenuItemResponse.class,
                    menuItemId
            );
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Unable to fetch menu item");
        }
    }
}
