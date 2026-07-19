package cl.duoc.ms_producer.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.queue.guias}")
    private String queueGuias;

    @Value("${rabbitmq.queue.guias.error}")
    private String queueError;

    @Value("${rabbitmq.exchange.direct}")
    private String exchangeDirect;

    @Value("${rabbitmq.routing-key.guias}")
    private String routingKeyGuias;

    @Value("${rabbitmq.exchange.dlx}")
    private String exchangeDLX;

    @Value("${rabbitmq.routing-key.dlx}")
    private String routingKeyDLX;

    // ==================== EXCHANGES ====================

    @Bean
    public DirectExchange guiasExchange() {
        return new DirectExchange(exchangeDirect);
    }

    @Bean
    public DirectExchange dlxExchange() {
        return new DirectExchange(exchangeDLX);
    }

    // ==================== COLAS ====================

    // Cola 1: mensajes normales (con DLX configurado)
    @Bean
    public Queue queueGuias() {
        return new Queue(queueGuias, true, false, false,
                Map.of("x-dead-letter-exchange", exchangeDLX,
                       "x-dead-letter-routing-key", routingKeyDLX));
    }

    // Cola 2: mensajes con error (Dead Letter Queue)
    @Bean
    public Queue queueError() {
        return new Queue(queueError, true);
    }

    // ==================== BINDINGS ====================

    @Bean
    public Binding bindingGuias() {
        return BindingBuilder.bind(queueGuias())
                .to(guiasExchange())
                .with(routingKeyGuias);
    }

    @Bean
    public Binding bindingError() {
        return BindingBuilder.bind(queueError())
                .to(dlxExchange())
                .with(routingKeyDLX);
    }
}