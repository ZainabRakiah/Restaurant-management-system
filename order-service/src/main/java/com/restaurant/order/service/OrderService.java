package com.restaurant.order.service;

import com.restaurant.common.dto.MenuItemResponse;
import com.restaurant.common.dto.OrderItemRequest;
import com.restaurant.common.dto.OrderItemResponse;
import com.restaurant.common.dto.OrderRequest;
import com.restaurant.common.dto.OrderResponse;
import com.restaurant.order.client.RestaurantClient;
import com.restaurant.order.model.Order;
import com.restaurant.order.model.OrderLine;
import com.restaurant.order.repository.OrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final RestaurantClient restaurantClient;

    public OrderService(OrderRepository orderRepository, RestaurantClient restaurantClient) {
        this.orderRepository = orderRepository;
        this.restaurantClient = restaurantClient;
    }

    public OrderResponse placeOrder(OrderRequest request) {
        Order order = new Order();
        order.setCustomerId(request.customerId());
        order.setRestaurantId(request.restaurantId());
        order.setDeliveryAddress(request.deliveryAddress());
        order.setStatus("PLACED");
        order.setCreatedAt(LocalDateTime.now());

        double total = 0.0;
        for (OrderItemRequest itemRequest : request.items()) {
            MenuItemResponse menuItem = restaurantClient.getMenuItem(itemRequest.menuItemId());
            if (!menuItem.available()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Menu item unavailable: " + menuItem.name());
            }
            if (!menuItem.restaurantId().equals(request.restaurantId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Menu item does not belong to restaurant");
            }

            OrderLine line = new OrderLine();
            line.setOrder(order);
            line.setMenuItemId(menuItem.id());
            line.setItemName(menuItem.name());
            line.setQuantity(itemRequest.quantity());
            line.setUnitPrice(menuItem.price());
            order.getLines().add(line);
            total += line.getLineTotal();
        }

        order.setTotalAmount(total);
        return toResponse(orderRepository.save(order));
    }

    public OrderResponse getOrder(Long id) {
        return toResponse(findOrder(id));
    }

    public List<OrderResponse> getOrdersByCustomer(Long customerId) {
        return orderRepository.findByCustomerId(customerId).stream().map(this::toResponse).toList();
    }

    public OrderResponse updateStatus(Long id, String status) {
        Order order = findOrder(id);
        order.setStatus(status);
        return toResponse(orderRepository.save(order));
    }

    private Order findOrder(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
    }

    private OrderResponse toResponse(Order order) {
        List<OrderItemResponse> items = order.getLines().stream()
                .map(line -> new OrderItemResponse(
                        line.getMenuItemId(),
                        line.getItemName(),
                        line.getQuantity(),
                        line.getUnitPrice(),
                        line.getLineTotal()
                ))
                .toList();

        return new OrderResponse(
                order.getId(),
                order.getCustomerId(),
                order.getRestaurantId(),
                order.getStatus(),
                order.getTotalAmount(),
                order.getDeliveryAddress(),
                items,
                order.getCreatedAt()
        );
    }
}
