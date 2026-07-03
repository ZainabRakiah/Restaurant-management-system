package com.restaurant.delivery.service;

import com.restaurant.common.dto.DeliveryRequest;
import com.restaurant.delivery.model.Delivery;
import com.restaurant.delivery.repository.DeliveryRepository;
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
class DeliveryServiceTest {

    @Mock
    private DeliveryRepository deliveryRepository;

    @InjectMocks
    private DeliveryService deliveryService;

    @Test
    void assignDelivery_shouldCreateAssignment() {
        DeliveryRequest request = new DeliveryRequest(10L, 3L, "Handle with care");
        when(deliveryRepository.findByOrderId(10L)).thenReturn(Optional.empty());
        when(deliveryRepository.save(any(Delivery.class))).thenAnswer(invocation -> {
            Delivery delivery = invocation.getArgument(0);
            delivery.setId(1L);
            return delivery;
        });

        var response = deliveryService.assignDelivery(request);

        assertThat(response.status()).isEqualTo("ASSIGNED");
        assertThat(response.orderId()).isEqualTo(10L);
    }

    @Test
    void assignDelivery_shouldRejectDuplicateOrder() {
        DeliveryRequest request = new DeliveryRequest(10L, 3L, null);
        Delivery existing = new Delivery();
        existing.setOrderId(10L);
        when(deliveryRepository.findByOrderId(10L)).thenReturn(Optional.of(existing));

        assertThatThrownBy(() -> deliveryService.assignDelivery(request))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("already assigned");
    }
}
