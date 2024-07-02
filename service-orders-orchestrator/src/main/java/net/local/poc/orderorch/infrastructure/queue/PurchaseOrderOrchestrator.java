package net.local.poc.orderorch.infrastructure.queue;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import net.local.poc.orderorch.application.dto.OrchestratorResponse;
import net.local.poc.orderorch.application.service.OrchestratorService;
import net.local.poc.orderorch.domain.orders.PurchaseOrder;

@Slf4j
@Component
public class PurchaseOrderOrchestrator {

    private final RabbitTemplate rabbitTemplate;
    private final OrchestratorService service;
    
    public PurchaseOrderOrchestrator(RabbitTemplate rabbitTemplate, OrchestratorService service) {
        this.rabbitTemplate = rabbitTemplate;
        this.service = service;
    }

    @RabbitListener(queues = {"ORDER_PLACED_TOPIC"})
    void onOrderPlacedEvent(Message<PurchaseOrder> message) {
        log.info("Received placed order {}", message.getPayload());
        service.processOrderFlow(message.getPayload())
               .doOnNext((response) -> sendOrderStatus(response));
    }

    void sendOrderStatus(OrchestratorResponse response) {
        log.info("Send process result: {}", response.message());
        var key = String.format("%s-order-response", response.orderId());
        rabbitTemplate.convertAndSend("ORDER_PLACED_TOPIC", response, new CorrelationData(key));
    }
}
