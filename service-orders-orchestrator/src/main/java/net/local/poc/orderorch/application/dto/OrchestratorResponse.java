package net.local.poc.orderorch.application.dto;

import java.util.UUID;

import net.local.poc.orderorch.domain.orders.OrderStatus;

public record OrchestratorResponse(UUID orderId, OrderStatus status, String message) {}
