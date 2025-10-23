package modulith_demo.ordermodule;

import modulith_demo.ordermodule.api.OrderApi;
import modulith_demo.ordermodule.api.OrderDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@ControllerAdvice
public class OrderController {

    private final OrderApi orderApi;

    public OrderController(OrderApi orderApi) {
        this.orderApi = orderApi;
    }

    /**
     * POST http://localhost:8080/api/orders?item=your-item-name
     * Example Response (200 OK):
     * {
     *   "id": "123e4567-e89b-12d3-a456-426614174000",
     *   "item": "Book",
     *   "status": "PENDING"
     * }
     */
    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestParam(required = false) String item) {
        OrderDto order = orderApi.createOrder(item);
        return ResponseEntity.ok(order);
    }

    /**
     * POST http://localhost:8080/api/orders/{orderId}/accept
     * Example Response (200 OK):
     * {
     *   "id": "123e4567-e89b-12d3-a456-426614174000",
     *   "item": "Book",
     *   "status": "ACCEPTED"
     * }
     */
    @PostMapping("/{orderId}/accept")
    public ResponseEntity<OrderDto> acceptOrder(@PathVariable String orderId) {
        OrderDto order = orderApi.acceptOrder(orderId);
        return ResponseEntity.ok(order);
    }

    /**
     * POST http://localhost:8080/api/orders/{orderId}/reject?reason=your-reason
     * Example Response (200 OK):
     * {
     *   "id": "123e4567-e89b-12d3-a456-426614174000",
     *   "item": "Book",
     *   "status": "REJECTED"
     * }
     */
    @PostMapping("/{orderId}/reject")
    public ResponseEntity<OrderDto> rejectOrder(
            @PathVariable String orderId,
            @RequestParam(defaultValue = "Order rejected") String reason) {
        OrderDto order = orderApi.rejectOrder(orderId, reason);
        return ResponseEntity.ok(order);
    }

    /**
     * GET http://localhost:8080/api/orders/{orderId}/status
     */
    @GetMapping("/{orderId}/status")
    public ResponseEntity<String> getOrderStatus(@PathVariable String orderId) {
        // In a real application, we would fetch the order from the database
        // For demo purposes, we'll just return a success message
        return ResponseEntity.ok("Order status retrieved for ID: " + orderId);
    }
}
