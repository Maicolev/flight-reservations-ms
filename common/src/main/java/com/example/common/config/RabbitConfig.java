package com.example.common.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitConfig {

    public static final String PENDING_QUEUE = "reservations.pending";
    public static final String CONFIRMED_QUEUE = "reservations.confirmed";
    public static final String ERROR_QUEUE = "reservations.errors";
    public static final String EXCHANGE_NAME = "reservation.exchange";

    @Bean
    public DirectExchange reservationExchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue pendingQueue() {
        return new Queue(PENDING_QUEUE, true);
    }

    @Bean
    public Queue confirmedQueue() {
        return new Queue(CONFIRMED_QUEUE, true);
    }

    @Bean
    public Queue errorQueue() {
        return new Queue(ERROR_QUEUE, true);
    }

    @Bean
    public Binding pendingBinding() {
        return BindingBuilder.bind(pendingQueue())
                .to(reservationExchange())
                .with("reservation.pending");
    }

    @Bean
    public Binding confirmedBinding() {
        return BindingBuilder.bind(confirmedQueue())
                .to(reservationExchange())
                .with("reservation.confirmed");
    }

    @Bean
    public Binding errorBinding() {
        return BindingBuilder.bind(errorQueue())
                .to(reservationExchange())
                .with("reservation.error");
    }

    @Bean
    public Jackson2JsonMessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }
}