package com.example.BillingService.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitConfig {
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }

    @Bean
    public Queue autoDeadLetterQueue() {
        return QueueBuilder.durable("bill.queue")
                .withArgument("x-message-ttl", 60000)
                .deadLetterExchange("") // Use default exchange
                .deadLetterRoutingKey("bill.queue.dlq")
                .build();
    }

    @Bean
    public Queue autoDeadLetterTarget() {
        return QueueBuilder.durable("bill.queue.dlq")
                .withArgument("x-message-ttl", 604800000)
                .build();
    }
}
