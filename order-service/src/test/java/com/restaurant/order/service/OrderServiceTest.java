package com.restaurant.order.service;

import com.restaurant.common.dto.MenuItemResponse;
import com.restaurant.common.dto.OrderItemRequest;
import com.restaurant.common.dto.OrderRequest;
import com.restaurant.order.client.RestaurantClient;
import com.restaurant.order.model.Order;
import com.restaurant.order.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private RestaurantClient restaurantClient;

    @InjectMocks
    private OrderService orderService;

    @Test
    void placeOrder_shouldCalculateTotal() {
        OrderRequest request = new OrderRequest(
                1L,
                10L,
                List.of(new OrderItemRequest(5L, 2)),
                "42 Oak Street"
        );

        when(restaurantClient.getMenuItem(5L)).thenReturn(
                new MenuItemResponse(5L, 10L, "Margherita", "Classic", 9.5, "Pizza", true));

        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setId(100L);
            return order;
        });

        var response = orderService.placeOrder(request);

        assertThat(response.totalAmount()).isEqualTo(19.0);
        assertThat(response.status()).isEqualTo("PLACED");
    }

    @Test
    void placeOrder_shouldRejectUnavailableItem() {
        OrderRequest request = new OrderRequest(
                1L,
                10L,
                List.of(new OrderItemRequest(5L, 1)),
                "42 Oak Street"
        );

        when(restaurantClient.getMenuItem(5L)).thenReturn(
                new MenuItemResponse(5L, 10L, "Margherita", "Classic", 9.5, "Pizza", false));

        assertThatThrownBy(() -> orderService.placeOrder(request))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("unavailable");
    }
}
