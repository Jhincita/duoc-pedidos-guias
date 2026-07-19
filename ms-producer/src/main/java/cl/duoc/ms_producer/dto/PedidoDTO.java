package cl.duoc.ms_producer.dto;


import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PedidoDTO {
    private String id;
    private String numeroPedido;
    private String transportista;
    private String cliente;
    private String origen;
    private String destino;
    private Double peso;
    private Double monto;
    private String estado;
    private LocalDateTime fechaCreacion;
}