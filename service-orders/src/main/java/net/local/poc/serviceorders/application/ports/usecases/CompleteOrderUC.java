package net.local.poc.serviceorders.application.ports.usecases;

import java.util.UUID;

public interface CompleteOrderUC {
    void execute(UUID orderId);
}
