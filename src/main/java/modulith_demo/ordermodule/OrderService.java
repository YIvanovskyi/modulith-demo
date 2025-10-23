package modulith_demo.ordermodule;

import modulith_demo.ordermodule.api.*;
import modulith_demo.ordermodule.exception.InvalidOrderOperationException;
import modulith_demo.ordermodule.exception.OrderNotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OrderService implements OrderApi {

    private final ApplicationEventPublisher eventPublisher;

    public OrderService(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    private final Map<String, Order> orderStore = new ConcurrentHashMap<>();

    @Override
    @Transactional
    public OrderDto createOrder(String item) {
        try {
            // Create order with a unique ID and initial status
            Order order = new Order();
            order.setId(UUID.randomUUID().toString());
            order.setItem(item);
            order.setStatus(OrderStatus.PENDING);
            
            // Store the order (in a real app, this would be a repository)
            orderStore.put(order.getId(), order);
            
            System.out.println("Created order: " + order);
            
            // Publish domain event with string ID converted to Long for compatibility
            eventPublisher.publishEvent(new OrderCreatedEvent(
                    Long.parseLong(order.getId().replaceAll("[^0-9]", "").substring(0, 10)),
                    order.getItem()
            ));
            
            return new OrderDto(order.getId(), order.getItem(), order.getStatus().name());
        } catch (Exception e) {
            throw new InvalidOrderOperationException("Failed to create order: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public OrderDto acceptOrder(String orderId) {
        if (!StringUtils.hasText(orderId)) {
            throw new InvalidOrderOperationException("Order ID cannot be empty");
        }

        Order order = orderStore.get(orderId);
        if (order == null) {
            throw new OrderNotFoundException(orderId);
        }

        // If order is already accepted, return current status
        if (order.getStatus() == OrderStatus.ACCEPTED) {
            return new OrderDto(order.getId(), order.getItem(), order.getStatus().name());
        }

        // If order is not in PENDING state, throw exception
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new InvalidOrderOperationException(
                String.format("Cannot accept order in %s status", order.getStatus())
            );
        }

        try {
            order.setStatus(OrderStatus.ACCEPTED);
            System.out.println("Order accepted: " + orderId);
            
            // Publish order processed event
            eventPublisher.publishEvent(new OrderProcessedEvent(
                    orderId,
                    OrderStatus.ACCEPTED,
                    "Order accepted successfully"
            ));
            
            return new OrderDto(order.getId(), order.getItem(), order.getStatus().name());
        } catch (Exception e) {
            throw new InvalidOrderOperationException("Failed to accept order: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDto getOrderStatus(String orderId) {
        Order order = orderStore.get(orderId);
        if (order == null) {
            throw new OrderNotFoundException(orderId);
        }
        return new OrderDto(order.getId(), order.getItem(), order.getStatus().name());
    }

    @Override
    @Transactional
    public OrderDto rejectOrder(String orderId, String reason) {
        if (!StringUtils.hasText(orderId)) {
            throw new InvalidOrderOperationException("Order ID cannot be empty");
        }
        if (!StringUtils.hasText(reason)) {
            throw new InvalidOrderOperationException("Rejection reason cannot be empty");
        }
        
        Order order = orderStore.get(orderId);
        if (order == null) {
            throw new OrderNotFoundException(orderId);
        }

        // If order is already rejected, return current status
        if (order.getStatus() == OrderStatus.REJECTED) {
            return new OrderDto(order.getId(), order.getItem(), order.getStatus().name());
        }

        // If order is not in PENDING state, throw exception
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new InvalidOrderOperationException(
                String.format("Cannot reject order in %s status", order.getStatus())
            );
        }

        try {
            order.setStatus(OrderStatus.REJECTED);
            System.out.println("Order rejected: " + orderId + " - Reason: " + reason);
            
            // Publish order processed event
            eventPublisher.publishEvent(new OrderProcessedEvent(
                    orderId,
                    OrderStatus.REJECTED,
                    "Order rejected: " + reason
            ));
            
            return new OrderDto(order.getId(), order.getItem(), order.getStatus().name());
        } catch (Exception e) {
            throw new InvalidOrderOperationException("Failed to reject order: " + e.getMessage(), e);
        }
    }
    
    // For demo purposes, we'll use this simple Order class
    public static class Order {
        private String id;
        private String item;
        private OrderStatus status;
        
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getItem() { return item; }
        public void setItem(String item) { this.item = item; }
        public OrderStatus getStatus() { return status; }
        public void setStatus(OrderStatus status) { this.status = status; }
        
        @Override
        public String toString() {
            return String.format("Order{id='%s', item='%s', status=%s}", 
                id, 
                item != null ? "'" + item + "'" : "null", 
                status
            );
        }
    }
}
