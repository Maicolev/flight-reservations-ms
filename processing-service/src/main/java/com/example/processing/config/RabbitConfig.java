package com.example.processing.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitConfig {
    @Bean
    public Queue confirmedQueue() {
        return new Queue("reservations.confirmed", true);
    }

    @Bean
    public Queue errorQueue() {
        return new Queue("reservations.errors", true);
    }
    @Bean

    public TopicExchange reservationExchange() {
        return new TopicExchange("reservationExchange");
    }

    @Bean
    public Binding confirmedBinding() {
        return BindingBuilder.bind(confirmedQueue())
                .to(reservationExchange())
                .with("reservation.confirmed");
    }

    @Bean
    public Jackson2JsonMessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }
}