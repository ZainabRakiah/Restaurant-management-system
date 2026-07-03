package com.restaurant.restaurant.service;

import com.restaurant.common.dto.MenuItemRequest;
import com.restaurant.common.dto.RestaurantRequest;
import com.restaurant.restaurant.model.MenuItem;
import com.restaurant.restaurant.model.Restaurant;
import com.restaurant.restaurant.repository.MenuItemRepository;
import com.restaurant.restaurant.repository.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private MenuItemRepository menuItemRepository;

    @InjectMocks
    private RestaurantService restaurantService;

    @Test
    void addMenuItem_shouldFailWhenRestaurantMissing() {
        MenuItemRequest request = new MenuItemRequest(99L, "Burger", "Tasty", 12.99, "Main");

        when(restaurantRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> restaurantService.addMenuItem(request))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Restaurant not found");
    }

    @Test
    void createRestaurant_shouldReturnCreatedRestaurant() {
        RestaurantRequest request = new RestaurantRequest("Pizza Palace", "Italian", "123 Main St", 4.5);
        Restaurant saved = new Restaurant();
        saved.setId(1L);
        saved.setName("Pizza Palace");
        saved.setCuisine("Italian");
        saved.setAddress("123 Main St");
        saved.setRating(4.5);
        saved.setActive(true);

        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(saved);

        var response = restaurantService.createRestaurant(request);

        assertThat(response.name()).isEqualTo("Pizza Palace");
        assertThat(response.active()).isTrue();
    }
}
