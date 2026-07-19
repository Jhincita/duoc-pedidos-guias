package cl.duoc.ms_producer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GuiaProducerService {

    private static final Logger log = LoggerFactory.getLogger(GuiaProducerService.class);

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.direct}")
    private String exchange;

    @Value("${rabbitmq.routing-key.guias}")
    private String routingKey;

    public GuiaProducerService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void enviarGuiaACola(String mensajeJson) {
        log.info("Enviando mensaje a Cola 1: {}", mensajeJson);
        rabbitTemplate.convertAndSend(exchange, routingKey, mensajeJson);
    }
}