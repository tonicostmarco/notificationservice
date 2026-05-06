package com.tonicostmarco.notificationservice.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {


    @Bean
    public Queue queuePaid() {

        return QueueBuilder.durable("payments.paid").build(); //durable significa que resiste a restart

    }

    @Bean
    public Queue queueFailed() {

        return QueueBuilder.durable("payments.failed").build(); //durable significa que resiste a restart

    }

    @Bean
    public Queue queuePending() {

        return QueueBuilder.durable("payments.pending").build(); //durable significa que resiste a restart

    }

    @Bean
    public TopicExchange exchange() {

        return ExchangeBuilder.topicExchange("exchange").build();
    }

    @Bean
    public Binding bindingPaid(TopicExchange topic, Queue queuePaid) {

        return BindingBuilder.bind(queuePaid).to(topic).with("payment.paid");

    }

    @Bean
    public Binding bindingFailed(TopicExchange topic, Queue queueFailed) {

        return BindingBuilder.bind(queueFailed).to(topic).with("payment.failed");

    }

    @Bean
    public Binding bindingPending(TopicExchange topic, Queue queuePending) {

        return BindingBuilder.bind(queuePending).to(topic).with("payment.pending");

    }
}
