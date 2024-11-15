---

# Spring Boot RabbitMQ Example

This project demonstrates how to use **RabbitMQ** with **Spring Boot** to implement a **Publisher-Consumer** example for message queuing.

## Table of Contents
- [Introduction](#introduction)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [API Usage](#api-usage)
- [Code Walkthrough](#code-walkthrough)
  - [Publisher](#publisher)
  - [Consumer](#consumer)
  - [Messaging Configuration](#messaging-configuration)
- [Troubleshooting](#troubleshooting)
- [Conclusion](#conclusion)

---

## Introduction
RabbitMQ is an open-source message broker that enables applications to communicate by sending and receiving messages asynchronously. This project covers the integration of RabbitMQ with Spring Boot to handle order processing using a Publisher-Consumer pattern.

## Prerequisites
Before you start, make sure you have the following installed:
- **Java 17** or later
- **Spring Boot 3.3.5**
- **RabbitMQ**
- **Postman** (or any other API testing tool)

## Installation

### Step 1: Install RabbitMQ on Windows
1. Download and install [ERlang](https://www.erlang.org/downloads).
2. Download and install [RabbitMQ](https://www.rabbitmq.com/docs/install-windows).
3. Go to the RabbitMQ Server install directory:
    ```bash
    C:\Program Files\RabbitMQ Server\rabbitmq_server-3.8.3\sbin
    ```
4. Enable RabbitMQ management plugin:
    ```bash
    rabbitmq-plugins enable rabbitmq_management
    ```
5. Open your browser and access the RabbitMQ Dashboard:
    - [http://localhost:15672](http://localhost:15672)
    - You can also use IP address: [http://127.0.0.1:15672](http://127.0.0.1:15672)
6. Default login credentials:
    - Username: `guest`
    - Password: `guest`

---

## Configuration

Add the following configuration in your `application.properties` file:

```properties
server.port=8081

# RabbitMQ Configuration
online.experiencedeveloper.queue=experiencedeveloper_queue
online.experiencedeveloper.exchange=experiencedeveloper_exchange
online.experiencedeveloper.routingKey=experiencedeveloper_routingKey
```

---

## Running the Application
To run the application, follow these steps:

1. Clone the repository:
   ```bash
   git clone <your-repo-url>
   ```
2. Navigate to the project directory:
   ```bash
   cd springboot-rabbitmq-example
   ```
3. Build the project using Maven:
   ```bash
   mvn clean install
   ```
4. Run the application:
   ```bash
   mvn spring-boot:run
   ```

---

## API Usage

### Create a New Order
Use Postman or any other tool to send a POST request:

**URL**: `http://localhost:8081/order/RedMirchi`

**Body**:
```json
{
    "name": "paneer-sabji, puri",
    "qty": 5,
    "price": 589.02
}
```

**Response**:
```
Success !!
```

---

## Code Walkthrough

### Publisher
The **OrderPublisher** class handles publishing messages to RabbitMQ:

```java
@RestController
@RequestMapping("/order")
public class OrderPublisher {

    @Autowired
    private RabbitTemplate template;

    @PostMapping("/{restaurantName}")
    public String bookOrder(@RequestBody Order order, @PathVariable String restaurantName) {
        order.setOrderId(UUID.randomUUID().toString());
        OrderStatus orderStatus = new OrderStatus(order, "PROGRESS", "Order placed successfully in " + restaurantName);
        template.convertAndSend(MessagingConfig.EXCHANGE, MessagingConfig.ROUTING_KEY, orderStatus);
        return "Success !!";
    }
}
```

### Consumer
The **User** class listens to the queue and consumes messages:

```java
@Component
public class User {

    @RabbitListener(queues = MessagingConfig.QUEUE)
    public void consumeMessageFromQueue(OrderStatus orderStatus) {
        System.out.println("Message received from queue: " + orderStatus);
    }
}
```

### Messaging Configuration
The **MessagingConfig** class handles the queue, exchange, and routing key setup:

```java
@Configuration
public class MessagingConfig {

    public static final String QUEUE = "experiencedeveloper_queue";
    public static final String EXCHANGE = "experiencedeveloper_exchange";
    public static final String ROUTING_KEY = "experiencedeveloper_routingKey";

    @Bean
    public Queue queue() {
        return new Queue(QUEUE);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }
}
```

### DTO Classes

- **Order.java**
```java
public class Order {
    private String orderId;
    private String name;
    private int qty;
    private double price;
}
```

- **OrderStatus.java**
```java
public class OrderStatus {
    private Order order;
    private String status;
    private String message;
}
```

---

## Troubleshooting
If you're unable to access the RabbitMQ management console:

1. Check RabbitMQ status:
    ```bash
    rabbitmqctl status
    ```
2. Restart the RabbitMQ service:
    ```bash
    rabbitmq-service.bat stop
    rabbitmq-service.bat start
    ```
3. Ensure port `15672` is open in your firewall settings.

Check logs for more information:
- Logs are typically located in:  
  `C:\Program Files\RabbitMQ Server\rabbitmq_server-x.x.x\logs`

---

## Conclusion
Congratulations! 🎉 You have successfully integrated RabbitMQ with Spring Boot to create a message-driven application. This project serves as a great starting point for more complex applications involving microservices and asynchronous communication.

Happy Coding! 🚀

---

Feel free to customize the README as per your project's specific needs.