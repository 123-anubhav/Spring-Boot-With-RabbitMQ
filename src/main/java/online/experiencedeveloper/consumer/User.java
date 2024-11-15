package online.experiencedeveloper.consumer;

import online.experiencedeveloper.config.MessagingConfig;
import online.experiencedeveloper.dto.OrderStatus;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class User {

	// CONSUMER TO CONSUME MESSAGE FROM MESSAGE QUEUE
	 @RabbitListener(queues = MessagingConfig.QUEUE)
	public void consumeMessageFromQueue(OrderStatus orderStatus) {
		System.out.println("Message recieved from queue : " + orderStatus);
	}
}
