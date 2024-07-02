package net.local.poc.serviceorders.infrastructure.queue;

import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import net.local.poc.serviceorders.application.ports.usecases.CancelOrderUC;
import net.local.poc.serviceorders.application.ports.usecases.CompleteOrderUC;
import net.local.poc.serviceorders.domain.entities.OrderStatus;
import net.local.poc.serviceorders.domain.events.OrderPlacedEvent;

@Slf4j
@Component
public class PurchaseOrderStatusManager {
    
    private final CancelOrderUC cancelOrderUC;
    private final CompleteOrderUC completeOrderUC;
    private final RabbitTemplate rabbitTemplate;

    public PurchaseOrderStatusManager(CancelOrderUC cancelOrderUC, CompleteOrderUC completeOrderUC, RabbitTemplate rabbitTemplate) {
        this.cancelOrderUC = cancelOrderUC;
        this.completeOrderUC = completeOrderUC;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Async
    @EventListener
    void onOrderPlacedEvent(OrderPlacedEvent event) {
        log.info("Process event: {}", event.getEventName());
        var key = String.format("%s-order-processing", event.getContent().getOrderId());
        rabbitTemplate.convertAndSend("ORDER_PLACED_TOPIC", event.getContent(), new CorrelationData(key));
    }

    @RabbitListener(queues = {"ORDER_ORCH_TOPIC"})
    void onOrderOrchastratorEvent(Message<OrchestratorEvent> message) {
        switch (message.getPayload().status) {
            case ORDER_CANCELLED:
                cancelOrderUC.execute(message.getPayload().orderId());
            break;
            case ORDER_COMPLETED:
                completeOrderUC.execute(message.getPayload().orderId());
            break;
            default:
                log.error("Invalid PurchaseOrder status", message.getPayload().status());
            break;
        }
        
        log.info("Received process order status {} with message {}", message.getPayload().status(), message.getPayload().message());
    }

    record OrchestratorEvent(UUID orderId, OrderStatus status, String message) {}
}
