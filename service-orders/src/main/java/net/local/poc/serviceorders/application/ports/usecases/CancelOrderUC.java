package net.local.poc.serviceorders.application.ports.usecases;

import java.util.UUID;

public interface CancelOrderUC {
    void execute(UUID orderId);
}
