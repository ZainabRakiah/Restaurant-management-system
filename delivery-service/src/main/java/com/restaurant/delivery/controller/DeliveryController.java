package com.restaurant.delivery.controller;

import com.restaurant.common.dto.DeliveryRequest;
import com.restaurant.common.dto.DeliveryResponse;
import com.restaurant.delivery.service.DeliveryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/deliveries")
public class DeliveryController {

    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DeliveryResponse assignDelivery(@Valid @RequestBody DeliveryRequest request) {
        return deliveryService.assignDelivery(request);
    }

    @GetMapping("/{id}")
    public DeliveryResponse getDelivery(@PathVariable Long id) {
        return deliveryService.getDelivery(id);
    }

    @GetMapping("/order/{orderId}")
    public DeliveryResponse getDeliveryByOrder(@PathVariable Long orderId) {
        return deliveryService.getDeliveryByOrder(orderId);
    }

    @PutMapping("/{id}/pickup")
    public DeliveryResponse markPickedUp(@PathVariable Long id) {
        return deliveryService.markPickedUp(id);
    }

    @PutMapping("/{id}/complete")
    public DeliveryResponse markDelivered(@PathVariable Long id) {
        return deliveryService.markDelivered(id);
    }
}
