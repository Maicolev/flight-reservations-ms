package com.example.reservation.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableRabbit
public class RabbitConfig {

    @Bean
    public TopicExchange reservationExchange() {
        return new TopicExchange("reservation.exchange");
    }

    @Bean
    public Queue pendingQueue() {
        return new Queue("reservations.pending", true);
    }

    @Bean
    public Binding pendingBinding() {
        return BindingBuilder.bind(pendingQueue())
                .to(reservationExchange())
                .with("reservation.pending");
    }

    @Bean
    public Jackson2JsonMessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }
}