package cl.duoc.ms_consumer.service;

import cl.duoc.ms_consumer.model.GuiaDespachoQ;
import cl.duoc.ms_consumer.repository.GuiaDespachoQRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class GuiaConsumerService {

    private static final Logger log = LoggerFactory.getLogger(GuiaConsumerService.class);

    private final GuiaDespachoQRepository queueRepository;
    private final ObjectMapper objectMapper;

    public GuiaConsumerService(GuiaDespachoQRepository queueRepository, ObjectMapper objectMapper) {
        this.queueRepository = queueRepository;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = "${rabbitmq.queue.guias}")
    public void consumirMensaje(String mensaje) {
        log.info("Mensaje recibido de Cola 1: {}", mensaje);
        try {
            JsonNode nodo = objectMapper.readTree(mensaje);

            GuiaDespachoQ registro = new GuiaDespachoQ(
                    nodo.path("guiaId").asText(),
                    nodo.path("transportista").asText(),
                    nodo.path("destino").asText(),
                    mensaje,
                    "PROCESADO"
            );

            queueRepository.save(registro);
            log.info("Guía guardada en tabla nueva: {}", registro.getGuiaId());

        } catch (Exception e) {
            log.error("Error procesando mensaje de Cola 1: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
