package cl.duoc.sistema_gestion_pedidos.repository;


import cl.duoc.sistema_gestion_pedidos.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface PedidoRepo extends JpaRepository<Pedido, String> {
    List<Pedido> findByTransportistaAndFechaCreacionBetween(String transportista,
                                                            LocalDateTime inicio,
                                                            LocalDateTime fin);
    List<Pedido> findByTransportista(String transportista);
}