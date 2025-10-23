package modulith_demo.ordermodule.exception;

import modulith_demo.common.exception.BaseException;

public class InvalidOrderOperationException extends BaseException {
    public InvalidOrderOperationException(String message) {
        super("Invalid order operation", "INVALID_ORDER_OPERATION", message);
    }

    public InvalidOrderOperationException(String message, Throwable cause) {
        super("Invalid order operation", "INVALID_ORDER_OPERATION", message, cause);
    }
}
