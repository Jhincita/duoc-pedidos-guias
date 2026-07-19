package cl.duoc.ms_producer.service;

import cl.duoc.ms_producer.dto.PedidoDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PedidosClient {

    private final RestTemplate restTemplate;
    private final String pedidosServiceUrl;

    public PedidosClient(@Value("${pedidos.service.url}") String pedidosServiceUrl) {
        this.restTemplate = new RestTemplate();
        this.pedidosServiceUrl = pedidosServiceUrl;
    }

    public PedidoDTO obtenerPedido(String pedidoId) {
        String url = pedidosServiceUrl + "/api/pedidos/" + pedidoId;
        return restTemplate.getForObject(url, PedidoDTO.class);
    }
}