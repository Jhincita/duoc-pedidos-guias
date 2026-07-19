package cl.duoc.ms_consumer.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "guias_despacho_queue")
public class GuiaDespachoQ {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String guiaId;
    private String transportista;
    private String destino;

    @Column(columnDefinition = "TEXT")
    private String contenidoMensaje;

    private String estado;

    @Column(name = "fecha_procesamiento")
    private LocalDateTime fechaProcesamiento;

    public GuiaDespachoQ() {}

    public GuiaDespachoQ(String guiaId, String transportista, String destino, String contenidoMensaje, String estado) {
        this.guiaId = guiaId;
        this.transportista = transportista;
        this.destino = destino;
        this.contenidoMensaje = contenidoMensaje;
        this.estado = estado;
        this.fechaProcesamiento = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getGuiaId() { return guiaId; }
    public void setGuiaId(String guiaId) { this.guiaId = guiaId; }

    public String getTransportista() { return transportista; }
    public void setTransportista(String transportista) { this.transportista = transportista; }

    public String getDestino() { return destino; }
    public void setDestino(String destino) { this.destino = destino; }

    public String getContenidoMensaje() { return contenidoMensaje; }
    public void setContenidoMensaje(String contenidoMensaje) { this.contenidoMensaje = contenidoMensaje; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public LocalDateTime getFechaProcesamiento() { return fechaProcesamiento; }
    public void setFechaProcesamiento(LocalDateTime fechaProcesamiento) { this.fechaProcesamiento = fechaProcesamiento; }
}
