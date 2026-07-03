package com.restaurant.restaurant.service;

import com.restaurant.common.dto.MenuItemRequest;
import com.restaurant.common.dto.MenuItemResponse;
import com.restaurant.common.dto.RestaurantRequest;
import com.restaurant.common.dto.RestaurantResponse;
import com.restaurant.restaurant.model.MenuItem;
import com.restaurant.restaurant.model.Restaurant;
import com.restaurant.restaurant.repository.MenuItemRepository;
import com.restaurant.restaurant.repository.RestaurantRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;

    public RestaurantService(RestaurantRepository restaurantRepository, MenuItemRepository menuItemRepository) {
        this.restaurantRepository = restaurantRepository;
        this.menuItemRepository = menuItemRepository;
    }

    public RestaurantResponse createRestaurant(RestaurantRequest request) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(request.name());
        restaurant.setCuisine(request.cuisine());
        restaurant.setAddress(request.address());
        restaurant.setRating(request.rating());
        return toRestaurantResponse(restaurantRepository.save(restaurant));
    }

    public RestaurantResponse getRestaurant(Long id) {
        return toRestaurantResponse(findRestaurant(id));
    }

    public List<RestaurantResponse> getActiveRestaurants() {
        return restaurantRepository.findByActiveTrue().stream().map(this::toRestaurantResponse).toList();
    }

    public MenuItemResponse addMenuItem(MenuItemRequest request) {
        findRestaurant(request.restaurantId());

        MenuItem item = new MenuItem();
        item.setRestaurantId(request.restaurantId());
        item.setName(request.name());
        item.setDescription(request.description());
        item.setPrice(request.price());
        item.setCategory(request.category());

        return toMenuItemResponse(menuItemRepository.save(item));
    }

    public List<MenuItemResponse> getMenuForRestaurant(Long restaurantId) {
        findRestaurant(restaurantId);
        return menuItemRepository.findByRestaurantIdAndAvailableTrue(restaurantId)
                .stream()
                .map(this::toMenuItemResponse)
                .toList();
    }

    public MenuItemResponse getMenuItem(Long id) {
        return toMenuItemResponse(findMenuItem(id));
    }

    private Restaurant findRestaurant(Long id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found"));
    }

    private MenuItem findMenuItem(Long id) {
        return menuItemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Menu item not found"));
    }

    private RestaurantResponse toRestaurantResponse(Restaurant restaurant) {
        return new RestaurantResponse(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getCuisine(),
                restaurant.getAddress(),
                restaurant.getRating(),
                restaurant.isActive()
        );
    }

    private MenuItemResponse toMenuItemResponse(MenuItem item) {
        return new MenuItemResponse(
                item.getId(),
                item.getRestaurantId(),
                item.getName(),
                item.getDescription(),
                item.getPrice(),
                item.getCategory(),
                item.isAvailable()
        );
    }
}
