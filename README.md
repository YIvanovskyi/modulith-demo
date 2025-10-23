# Modulith Demo

A Spring Boot application demonstrating the Spring Modulith architecture pattern, which helps in building well-structured, maintainable, and testable modular monoliths.

## 🚀 Features

- **Modular Architecture**: Organized into distinct modules using Spring Modulith
- **Event-Driven**: Implements domain events for inter-module communication
- **RESTful API**: Exposes endpoints for order management
- **Testable**: Comprehensive test coverage with unit and integration tests

## 🏗️ Project Structure

```
src/main/java/modulith_demo/
├── ModulithDemoApplication.java  # Main application class
├── ordermodule/                 # Order management module
│   ├── OrderController.java     # REST controller for order operations
│   ├── OrderService.java        # Business logic for order processing
│   ├── Order.java               # Domain model for orders
│   ├── OrderStatus.java         # Order status enum
│   └── exception/               # Order specific exceptions
│       ├── InvalidOrderOperationException.java  # Thrown for invalid order operations
│       └── OrderNotFoundException.java          # Thrown when order is not found
│   └── api/                     # Module's public API
│       ├── OrderApi.java        # Internal API interface
│       ├── OrderDto.java        # Data transfer object
│       ├── OrderCreatedEvent.java # Event published when order is created
│       ├── OrderProcessedEvent.java # Event published when order is processed
│       └── package-info.java    # Package metadata
├── notificationmodule/          # Notification module
│   ├── NotificationService.java # Handles notifications
│   └── EmailNotification.java   # Email notification model
└── common/                      # Shared components
    └── exception/               # Common exception handling
        ├── BaseException.java    # Base exception class
        ├── ErrorResponse.java    # Standard error response format
        └── GlobalExceptionHandler.java # Global exception handler
```

## 🛠️ Prerequisites

- Java 21 or later
- Maven 3.6.3 or later
- IntelliJ IDEA Community Edition

## 🚀 Getting Started

1. **Clone the repository**
   ```bash
   git clone https://github.com/your-username/modulith-demo.git
   cd modulith-demo
   ```

2. **Build the project**
   ```bash
   ./mvnw clean install
   ```

3. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```

## 🌐 API Endpoints

### Orders
- `POST /api/orders` - Create a new order
  ```bash
  curl -X POST "http://localhost:8080/api/orders?item=Book"
  ```

- `POST /api/orders/{orderId}/accept` - Accept an order
  ```bash
  curl -X POST "http://localhost:8080/api/orders/123/accept"
  ```

- `POST /api/orders/{orderId}/reject` - Reject an order
  ```bash
  curl -X POST "http://localhost:8080/api/orders/123/reject?reason=Out%20of%20stock"
  ```

- `GET /api/orders/{orderId}/status` - Get order status
  ```bash
  curl "http://localhost:8080/api/orders/123/status"
  ```

## 🧪 Running Tests

Run all tests:
```bash
./mvnw test
```

Run a specific test class:
```bash
./mvnw test -Dtest=OrderServiceTest
```

## 🏗️ Module Structure

### Order Module
Handles order processing and management.
- **API**: Exposes REST endpoints for order operations
- **Events**: Publishes domain events for order state changes

### Notification Module
Handles sending notifications.
- Listens to order events
- Sends appropriate notifications

## 📚 Technologies Used

- **Spring Boot 3.5.6** - Application framework
- **Spring Modulith** - For modular monolith architecture
- **Lombok** - For reducing boilerplate code
- **JUnit 5** - For unit and integration testing
- **Maven** - Dependency management
