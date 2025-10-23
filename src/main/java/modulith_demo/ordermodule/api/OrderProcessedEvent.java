package modulith_demo.ordermodule.api;

import modulith_demo.ordermodule.OrderStatus;

public record OrderProcessedEvent(String orderId, OrderStatus status, String message) {}