package modulith_demo.notificationmodule;

import modulith_demo.ordermodule.api.OrderCreatedEvent;
import modulith_demo.ordermodule.api.OrderProcessedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
public class NotificationService {


    @EventListener
    @Async
    public void handleOrderCreated(OrderCreatedEvent event) {
        String subject = "Order #" + event.orderId()+ " Confirmation";
        String message = "Thank you for your order of: " + event.item();
        
        EmailNotification email = new EmailNotification("user@example.com", subject, message);
        sendEmail(email);
    }

    @Async
    @TransactionalEventListener
    public void handleOrderProcessed(OrderProcessedEvent event) {
        String subject = "Order #" + event.orderId() + " Update: " + event.status();
        String message = "Your order status has been updated: " + event.message();
        
        EmailNotification email = new EmailNotification(
            "user@example.com", 
            subject, 
            message
        );
        
        sendEmail(email);
    }
    
    private void sendEmail(EmailNotification email) {
        // In a real application, this would use an email service
        System.out.println("Sending email notification: " + email);
    }
}