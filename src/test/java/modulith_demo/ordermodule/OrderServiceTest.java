package modulith_demo.ordermodule;

import modulith_demo.ordermodule.api.OrderDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.modulith.test.EnableScenarios;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EnableScenarios
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Test
    void testCreateOrder() {
        // When
        OrderDto order = orderService.createOrder("Book");

        // Then
        assertNotNull(order, "Order should not be null");
        assertNotNull(order.id(), "Order ID should be generated");
        assertEquals("Book", order.item(), "Order item should match");
        assertEquals("PENDING", order.status(), "New order should be PENDING");
    }

    @Test
    void testCreateOrderWithEmptyItem() {
        // When
        OrderDto order = orderService.createOrder("");

        // Then
        assertNotNull(order, "Order should be created even with empty item");
        assertEquals("", order.item(), "Order item should be empty string");
    }

    @Test
    void testCreateOrderWithNullItem() {
        // When
        OrderDto order = orderService.createOrder(null);

        // Then
        assertNotNull(order, "Order should be created even with null item");
        assertNull(order.item(), "Order item should be null");
    }

    @Test
    void testOrderStatusFlow() {
        // Create order
        OrderDto order = orderService.createOrder("Laptop");
        assertEquals("PENDING", order.status());

        // Accept order
        OrderDto acceptedOrder = orderService.acceptOrder(order.id());
        assertEquals("ACCEPTED", acceptedOrder.status());

        // Test that we can't accept an already accepted order
        OrderDto sameOrder = orderService.acceptOrder(order.id());
        assertEquals("ACCEPTED", sameOrder.status());
    }

    @Test
    void testRejectOrder() {
        // Create order
        OrderDto order = orderService.createOrder("Monitor");
        
        // Reject with reason
        String rejectReason = "Out of stock";
        OrderDto rejectedOrder = orderService.rejectOrder(order.id(), rejectReason);
        
        // Verify status and that we can't modify a rejected order
        assertEquals("REJECTED", rejectedOrder.status());
        OrderDto sameOrder = orderService.rejectOrder(order.id(), "Another reason");
        assertEquals("REJECTED", sameOrder.status());
    }
}