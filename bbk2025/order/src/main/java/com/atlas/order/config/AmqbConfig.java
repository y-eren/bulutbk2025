package com.atlas.order.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmqbConfig {
    public static final String EXCHANGE = "payments.exchange";
    public static final String QUEUE    = "payments.queue";
    public static final String ROUTING  = "pay";

    @Bean TopicExchange paymentsExchange() { return new TopicExchange(EXCHANGE, true, false); }
    @Bean Queue paymentsQueue() { return new Queue(QUEUE, true); }
    @Bean Binding bind() { return BindingBuilder.bind(paymentsQueue()).to(paymentsExchange()).with(ROUTING); }

    @Bean Jackson2JsonMessageConverter messageConverter() { return new Jackson2JsonMessageConverter(); }

    @Bean RabbitTemplate rabbitTemplate(ConnectionFactory cf) {
        var t = new RabbitTemplate(cf);
        t.setMessageConverter(messageConverter());
        return t;
    }
}
