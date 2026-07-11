package cl.duoc.ms_guias.repository;

import cl.duoc.ms_guias.model.GuiaDespachoQ;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuiaDespachoQRepository extends JpaRepository<GuiaDespachoQ, Long> {
}