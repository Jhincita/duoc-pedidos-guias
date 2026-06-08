package cl.duoc.sistema_gestion_pedidos.controller;


import cl.duoc.sistema_gestion_pedidos.model.Pedido;
import cl.duoc.sistema_gestion_pedidos.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    // create pedido
    @PostMapping
    public ResponseEntity<Pedido> crearPedido(@RequestBody Pedido pedido) {
        Pedido nuevo = pedidoService.crearPedido(pedido);
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
    }

    // get pedido por id
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> obtenerPedido(@PathVariable String id) {
        return ResponseEntity.ok(pedidoService.obtenerPedido(id));
    }

    // update pedido
    @PutMapping("/{id}")
    public ResponseEntity<Pedido> actualizarPedido(@PathVariable String id,
                                                   @RequestBody Pedido pedidoActualizado) {
        return ResponseEntity.ok(pedidoService.actualizarPedido(id, pedidoActualizado));
    }

    // delete pedido
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPedido(@PathVariable String id) {
        pedidoService.eliminarPedido(id);
        return ResponseEntity.noContent().build();
    }

    // get  pedidos por transportista y rango de fechas
    @GetMapping("/consultar")
    public ResponseEntity<List<Pedido>> consultarPedidos(
            @RequestParam String transportista,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        return ResponseEntity.ok(pedidoService.consultarPedidos(transportista, fechaInicio, fechaFin));
    }

    // get all
    @GetMapping
    public ResponseEntity<List<Pedido>> obtenerTodos() {
        return ResponseEntity.ok(pedidoService.consultarPedidos(null, null, null));
    }
}