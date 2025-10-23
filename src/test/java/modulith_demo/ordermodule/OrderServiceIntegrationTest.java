package modulith_demo.ordermodule;

import modulith_demo.notificationmodule.NotificationService;
import modulith_demo.ordermodule.api.OrderCreatedEvent;
import modulith_demo.ordermodule.api.OrderDto;
import modulith_demo.ordermodule.api.OrderProcessedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.PublishedEvents;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


@ApplicationModuleTest
@ActiveProfiles("test")
public class OrderServiceIntegrationTest {

    @MockitoBean
    private NotificationService notificationService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private PublishedEvents publishedEvents;

    @BeforeEach
    void setUp() {
        reset(notificationService);
    }

    @Test
    void testOrderCreatedEventIsPublished() {
        orderService.createOrder("Keyboard");

        assertThat(publishedEvents.ofType(OrderCreatedEvent.class))
                .hasSize(1)
                .first()
                .satisfies(event ->
                        assertThat(event.item())
                                .isEqualTo("Keyboard")
                );
    }

    @Test
    void testOrderAcceptedEvent() throws InterruptedException {
        var order = orderService.createOrder("Mouse");

        TransactionTemplate tx = new TransactionTemplate(transactionManager);
        tx.execute(status -> {
            orderService.acceptOrder(order.id());
            return null;
        });

        Thread.sleep(2000); // wait for async

        verify(notificationService, timeout(5000))
                .handleOrderProcessed(argThat(e -> e.status() == OrderStatus.ACCEPTED));
    }

    @Test
    void testOrderLifecycle() throws Exception {
        String item = "Mouse";

        OrderDto order = orderService.createOrder(item);

        assertNotNull(order);
        assertEquals(item, order.item());
        assertEquals("PENDING", order.status());

        ArgumentCaptor<OrderCreatedEvent> createdCaptor = ArgumentCaptor.forClass(OrderCreatedEvent.class);
        verify(notificationService, timeout(5000)).handleOrderCreated(createdCaptor.capture());
        assertEquals(item, createdCaptor.getValue().item());

        reset(notificationService);

        TransactionTemplate tx = new TransactionTemplate(transactionManager);
        tx.execute(status -> {
            orderService.acceptOrder(order.id());
            return null;
        });

        Thread.sleep(1500);

        ArgumentCaptor<OrderProcessedEvent> processedCaptor = ArgumentCaptor.forClass(OrderProcessedEvent.class);
        verify(notificationService, timeout(5000)).handleOrderProcessed(processedCaptor.capture());
        assertEquals(OrderStatus.ACCEPTED, processedCaptor.getValue().status());
    }

    @Test
    void testRejectOrderPublishesEvent() {
        // First create an order
        OrderDto createdOrder = orderService.createOrder("Test Item");
        
        // Then reject it
        var order = orderService.rejectOrder(createdOrder.id(), "Out of stock");
        assertThat(order.status()).isEqualTo("REJECTED");

        verify(notificationService, timeout(5000))
                .handleOrderProcessed(argThat(e -> e.status() == OrderStatus.REJECTED));
    }
}




