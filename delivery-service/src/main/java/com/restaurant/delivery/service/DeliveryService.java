package com.restaurant.delivery.service;

import com.restaurant.common.dto.DeliveryRequest;
import com.restaurant.common.dto.DeliveryResponse;
import com.restaurant.delivery.model.Delivery;
import com.restaurant.delivery.repository.DeliveryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    public DeliveryService(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    public DeliveryResponse assignDelivery(DeliveryRequest request) {
        if (deliveryRepository.findByOrderId(request.orderId()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Delivery already assigned for order");
        }

        Delivery delivery = new Delivery();
        delivery.setOrderId(request.orderId());
        delivery.setDriverId(request.driverId());
        delivery.setNotes(request.notes());
        delivery.setStatus("ASSIGNED");
        delivery.setAssignedAt(LocalDateTime.now());

        return toResponse(deliveryRepository.save(delivery));
    }

    public DeliveryResponse getDelivery(Long id) {
        return toResponse(findDelivery(id));
    }

    public DeliveryResponse getDeliveryByOrder(Long orderId) {
        return deliveryRepository.findByOrderId(orderId)
                .map(this::toResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Delivery not found"));
    }

    public DeliveryResponse markPickedUp(Long id) {
        Delivery delivery = findDelivery(id);
        delivery.setStatus("PICKED_UP");
        return toResponse(deliveryRepository.save(delivery));
    }

    public DeliveryResponse markDelivered(Long id) {
        Delivery delivery = findDelivery(id);
        delivery.setStatus("DELIVERED");
        delivery.setDeliveredAt(LocalDateTime.now());
        return toResponse(deliveryRepository.save(delivery));
    }

    private Delivery findDelivery(Long id) {
        return deliveryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Delivery not found"));
    }

    private DeliveryResponse toResponse(Delivery delivery) {
        return new DeliveryResponse(
                delivery.getId(),
                delivery.getOrderId(),
                delivery.getDriverId(),
                delivery.getStatus(),
                delivery.getNotes(),
                delivery.getAssignedAt(),
                delivery.getDeliveredAt()
        );
    }
}
