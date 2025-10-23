package modulith_demo.ordermodule.exception;

import modulith_demo.common.exception.BaseException;

public class OrderNotFoundException extends BaseException {
    public OrderNotFoundException(String orderId) {
        super("Order not found", "ORDER_NOT_FOUND", String.format("Order with id %s not found", orderId));
    }
}
