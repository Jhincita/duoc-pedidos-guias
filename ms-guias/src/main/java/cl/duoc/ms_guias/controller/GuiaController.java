package cl.duoc.ms_guias.controller;

import cl.duoc.ms_guias.dto.PedidoDTO;
import cl.duoc.ms_guias.model.Guia;
import cl.duoc.ms_guias.service.GuiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/guias")
public class GuiaController {

    @Autowired
    private GuiaService guiaService;

    // Crear guía a partir de un pedido existente
    @PostMapping("/crear-desde-pedido/{pedidoId}")
    public ResponseEntity<Guia> crearGuia(@PathVariable String pedidoId) throws IOException {
        Guia guia = guiaService.crearGuiaDesdePedido(pedidoId);
        return new ResponseEntity<>(guia, HttpStatus.CREATED);
    }

    // Descargar guía por id (sin validación manual de transportista vía header)
    @GetMapping("/{id}/descargar")
    public ResponseEntity<byte[]> descargarGuia(@PathVariable String id) throws IOException {
        byte[] pdf = guiaService.descargarGuia(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "guia_" + id + ".pdf");
        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }

    // Modificar guía (actualiza datos y regenera PDF)
    @PutMapping("/{id}")
    public ResponseEntity<Guia> actualizarGuia(@PathVariable String id,
                                               @RequestBody PedidoDTO pedidoActualizado) throws IOException {
        return ResponseEntity.ok(guiaService.actualizarGuia(id, pedidoActualizado));
    }

    // Eliminar guía
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarGuia(@PathVariable String id) {
        guiaService.eliminarGuia(id);
        return ResponseEntity.noContent().build();
    }

    // Consultar guías por transportista y rango de fechas
    @GetMapping("/consultar")
    public ResponseEntity<List<Guia>> consultarGuias(
            @RequestParam String transportista,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        return ResponseEntity.ok(guiaService.consultarGuias(transportista, fechaInicio, fechaFin));
    }

    // get all
    @GetMapping
    public ResponseEntity<List<Guia>> obtenerTodas() {
        return ResponseEntity.ok(guiaService.obtenerTodasLasGuias());
    }
}