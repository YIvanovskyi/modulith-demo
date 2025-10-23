package modulith_demo.ordermodule.api;

import modulith_demo.ordermodule.OrderService.Order;

public record OrderDto(
   String id,
    String item,
    String status
) {
   public static OrderDto from(Order order) {
       return new OrderDto(
            order.getId(),
            order.getItem(),
            order.getStatus().name()
        );
    }
}
