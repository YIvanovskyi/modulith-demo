package modulith_demo.notificationmodule;

public record EmailNotification(String recipient,
                                String subject,
                                String message) {

}
