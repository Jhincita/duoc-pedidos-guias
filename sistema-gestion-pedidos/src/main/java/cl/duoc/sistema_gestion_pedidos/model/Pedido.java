package cl.duoc.sistema_gestion_pedidos.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String numeroPedido;
    private String transportista;       // Empresa transportista
    private String cliente;
    private String origen;
    private String destino;
    private Double peso;
    private Double monto;
    private String estado;              // PENDIENTE, EN_PROCESO, ENTREGADO, CANCELADO
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}