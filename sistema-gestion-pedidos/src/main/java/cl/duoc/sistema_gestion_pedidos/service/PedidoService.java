package cl.duoc.sistema_gestion_pedidos.service;


import cl.duoc.sistema_gestion_pedidos.model.Pedido;
import cl.duoc.sistema_gestion_pedidos.repository.PedidoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PedidoService {

  @Autowired
    private PedidoRepo pedidoRepo;

    public Pedido crearPedido(Pedido pedido) {
        pedido.setFechaCreacion(LocalDateTime.now());
        pedido.setFechaActualizacion(LocalDateTime.now());
        pedido.setEstado("PENDIENTE");
        return pedidoRepo.save(pedido);
    }

    public Pedido obtenerPedido(String id) {
        return pedidoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
    }

    public Pedido actualizarPedido(String id, Pedido actualizado) {
        Pedido existente = obtenerPedido(id);
        existente.setCliente(actualizado.getCliente());
        existente.setOrigen(actualizado.getOrigen());
        existente.setDestino(actualizado.getDestino());
        existente.setPeso(actualizado.getPeso());
        existente.setMonto(actualizado.getMonto());
        existente.setEstado(actualizado.getEstado());
        existente.setFechaActualizacion(LocalDateTime.now());
        return pedidoRepo.save(existente);
    }

    public void eliminarPedido(String id) {
        pedidoRepo.deleteById(id);
    }

    public List<Pedido> consultarPedidos(String transportista, LocalDateTime inicio, LocalDateTime fin) {
        if (transportista == null && inicio == null && fin == null) {
            return pedidoRepo.findAll();  // o usa un método findAll()
        }
        if (transportista != null && inicio != null && fin != null) {
            return pedidoRepo.findByTransportistaAndFechaCreacionBetween(transportista, inicio, fin);
        }
        // Agrega más combinaciones si es necesario (solo transportista, solo fechas, etc.)
        throw new RuntimeException("Filtros incompletos");
    }
}