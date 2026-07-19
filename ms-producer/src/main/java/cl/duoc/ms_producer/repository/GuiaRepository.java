package cl.duoc.ms_producer.repository;


import cl.duoc.ms_producer.model.Guia;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface GuiaRepository extends JpaRepository<Guia, String> {
    List<Guia> findByTransportistaAndFechaGeneracionBetween(String transportista,
                                                            LocalDateTime start,
                                                            LocalDateTime end);
}