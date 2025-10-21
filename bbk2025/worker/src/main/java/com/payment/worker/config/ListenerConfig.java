package com.payment.worker.config;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.Jackson2JavaTypeMapper.TypePrecedence;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ListenerConfig {

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        var converter = new Jackson2JsonMessageConverter();
        // Header'daki __TypeId__ ne olursa olsun, metod parametresinin tipine göre deserialize et
        var mapper = new DefaultJackson2JavaTypeMapper();
        mapper.setTypePrecedence(TypePrecedence.INFERRED);
        mapper.addTrustedPackages("*");
        converter.setJavaTypeMapper(mapper);
        return converter;
    }

    @Bean(name = "rabbitListenerContainerFactory")
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory cf, Jackson2JsonMessageConverter conv) {
        var f = new SimpleRabbitListenerContainerFactory();
        f.setConnectionFactory(cf);
        f.setMessageConverter(conv);
        return f;
    }
}
