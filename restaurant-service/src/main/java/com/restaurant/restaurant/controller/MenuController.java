package com.restaurant.restaurant.controller;

import com.restaurant.common.dto.MenuItemRequest;
import com.restaurant.common.dto.MenuItemResponse;
import com.restaurant.restaurant.service.RestaurantService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuController {

    private final RestaurantService restaurantService;

    public MenuController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MenuItemResponse addMenuItem(@Valid @RequestBody MenuItemRequest request) {
        return restaurantService.addMenuItem(request);
    }

    @GetMapping("/restaurant/{restaurantId}")
    public List<MenuItemResponse> getMenu(@PathVariable Long restaurantId) {
        return restaurantService.getMenuForRestaurant(restaurantId);
    }

    @GetMapping("/{id}")
    public MenuItemResponse getMenuItem(@PathVariable Long id) {
        return restaurantService.getMenuItem(id);
    }
}
