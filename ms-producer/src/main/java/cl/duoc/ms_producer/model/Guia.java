package cl.duoc.ms_producer.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Guia {
    @Id
    private String id;
    private String pedidoId;
    private String transportista;
    private String destinatario;
    private String origen;
    private String destino;
    private Double peso;
    private Double monto;
    private String s3Key;
    private LocalDateTime fechaGeneracion;
    private LocalDateTime fechaActualizacion;
    private String estado;         // GENERADA, SUBIDA, ELIMINADA
}