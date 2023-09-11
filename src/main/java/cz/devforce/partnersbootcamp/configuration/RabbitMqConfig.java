package cz.devforce.partnersbootcamp.configuration;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMqConfig {

    @Bean
    public Declarables directBindings(RabbitMqProperties rabbitMqProperties) {
        var defaultQueue = new Queue(rabbitMqProperties.getDefaultQueue(), true);
        var fileUploadedQueue = new Queue(rabbitMqProperties.getFileUploadedQueue(), true);
        var directExchange = ExchangeBuilder.directExchange(rabbitMqProperties.getExchange()).durable(true).build();

        return new Declarables(
            defaultQueue,
            fileUploadedQueue,
            ExchangeBuilder.directExchange(rabbitMqProperties.getExchange()).durable(true).build(),
            BindingBuilder.bind(defaultQueue).to(directExchange).with(rabbitMqProperties.getDefaultRoutingKey()).noargs(),
            BindingBuilder.bind(fileUploadedQueue).to(directExchange).with(rabbitMqProperties.getFileUploadedRoutingKey()).noargs());
    }

    @Bean
    public CachingConnectionFactory connectionFactory(RabbitMqProperties rabbitMqProperties) {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(rabbitMqProperties.getHost());
        cachingConnectionFactory.setUsername(rabbitMqProperties.getUsername());
        cachingConnectionFactory.setPassword(rabbitMqProperties.getPassword());
        return cachingConnectionFactory;
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
