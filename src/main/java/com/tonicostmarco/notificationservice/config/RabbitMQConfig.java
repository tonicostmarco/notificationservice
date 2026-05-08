package com.tonicostmarco.notificationservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue queuePaid() {

        return QueueBuilder.durable("payment.paid").build();

    }

    @Bean
    public Queue queueFailed() {

        return QueueBuilder.durable("payment.failed").build();

    }

    @Bean
    public Queue queuePending() {

        return QueueBuilder.durable("payment.pending").build();

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

    @Bean
    public JacksonJsonMessageConverter messageConverter() {

      return new JacksonJsonMessageConverter();

    }
}
