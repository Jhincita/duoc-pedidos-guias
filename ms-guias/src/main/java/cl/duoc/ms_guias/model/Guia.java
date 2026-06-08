package cl.duoc.ms_guias.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Guia {
    @Id
    private String id;            // puede ser el mismo ID del pedido o UUID
    private String pedidoId;
    private String transportista;
    private String destinatario;
    private String origen;
    private String destino;
    private Double peso;
    private Double monto;
    private String s3Key;          // clave en S3 (ej: 202511/transportistaX/guia_abc.pdf)
    private LocalDateTime fechaGeneracion;
    private LocalDateTime fechaActualizacion;
    private String estado;         // GENERADA, SUBIDA, ELIMINADA
}