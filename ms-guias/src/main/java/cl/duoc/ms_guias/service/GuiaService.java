package cl.duoc.ms_guias.service;

import cl.duoc.ms_guias.dto.PedidoDTO;
import cl.duoc.ms_guias.model.Guia;
import cl.duoc.ms_guias.repository.GuiaRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class GuiaService {

    @Autowired
    private GuiaRepository guiaRepository;

    @Autowired
    private PedidosClient pedidosClient;

    @Autowired
    private PDFGenerationService pdfService;

    @Autowired
    private EFSStorageService efsStorage;

    @Autowired
    private S3StorageService s3Storage;

    // Crear guía a partir de un pedido existente
    public Guia crearGuiaDesdePedido(String pedidoId) throws IOException {
        PedidoDTO pedido = pedidosClient.obtenerPedido(pedidoId);

        // 1. Generar PDF
        byte[] pdfContent = pdfService.generateGuiaPdf(pedido);

        // 2. Guardar temporalmente en EFS
        String tempPath = efsStorage.saveTempFile(pdfContent, ".pdf");

        // 3. Subir a S3
        String s3Key = s3Storage.uploadGuia(pedido.getTransportista(), pedido.getId(), pdfContent);

        // 4. Guardar metadatos en BD
        Guia guia = new Guia();
        guia.setId(pedido.getId());  // reutilizamos el mismo ID del pedido
        guia.setPedidoId(pedido.getId());
        guia.setTransportista(pedido.getTransportista());
        guia.setDestinatario(pedido.getCliente());
        guia.setOrigen(pedido.getOrigen());
        guia.setDestino(pedido.getDestino());
        guia.setPeso(pedido.getPeso());
        guia.setMonto(pedido.getMonto());
        guia.setS3Key(s3Key);
        guia.setFechaGeneracion(LocalDateTime.now());
        guia.setFechaActualizacion(LocalDateTime.now());
        guia.setEstado("SUBIDA");

        // Limpiar temporal
        efsStorage.deleteTempFile(tempPath);

        return guiaRepository.save(guia);
    }

    // Descargar guía
    public byte[] descargarGuia(String guiaId) throws IOException {
        Guia guia = guiaRepository.findById(guiaId)
                .orElseThrow(() -> new RuntimeException("Guía no encontrada"));
        return s3Storage.downloadGuia(guia.getS3Key());
    }

    // Actualizar guía (regenera PDF y reemplaza en S3)
    public Guia actualizarGuia(String guiaId, PedidoDTO pedidoActualizado) throws IOException {
        Guia guia = guiaRepository.findById(guiaId)
                .orElseThrow(() -> new RuntimeException("Guía no encontrada"));

        byte[] newPdf = pdfService.generateGuiaPdf(pedidoActualizado);
        // Actualizar en S3 (sobrescribir)
        String newS3Key = s3Storage.uploadGuia(pedidoActualizado.getTransportista(), guiaId, newPdf);
        // Si la key cambió, borrar la anterior (opcional)
        if (!newS3Key.equals(guia.getS3Key())) {
            s3Storage.deleteGuia(guia.getS3Key());
        }

        guia.setTransportista(pedidoActualizado.getTransportista());
        guia.setDestinatario(pedidoActualizado.getCliente());
        guia.setOrigen(pedidoActualizado.getOrigen());
        guia.setDestino(pedidoActualizado.getDestino());
        guia.setPeso(pedidoActualizado.getPeso());
        guia.setMonto(pedidoActualizado.getMonto());
        guia.setS3Key(newS3Key);
        guia.setFechaActualizacion(LocalDateTime.now());
        guia.setEstado("ACTUALIZADA");
        return guiaRepository.save(guia);
    }

    // Eliminar guía (de S3 y BD)
    public void eliminarGuia(String guiaId) {
        Guia guia = guiaRepository.findById(guiaId)
                .orElseThrow(() -> new RuntimeException("Guía no encontrada"));
        s3Storage.deleteGuia(guia.getS3Key());
        guiaRepository.deleteById(guiaId);
    }

    // Consultar guías por transportista y rango de fechas
    public List<Guia> consultarGuias(String transportista, LocalDateTime inicio, LocalDateTime fin) {
        return guiaRepository.findByTransportistaAndFechaGeneracionBetween(transportista, inicio, fin);
    }

    // Obtener todas las guías sin filtro
    public List<Guia> obtenerTodasLasGuias() {
        return guiaRepository.findAll();
    }
}