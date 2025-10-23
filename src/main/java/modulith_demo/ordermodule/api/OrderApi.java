package modulith_demo.ordermodule.api;

public interface OrderApi {
    OrderDto createOrder(String item);
    OrderDto acceptOrder(String orderId);
    OrderDto rejectOrder(String orderId, String reason);
    OrderDto getOrderStatus(String orderId);
}
