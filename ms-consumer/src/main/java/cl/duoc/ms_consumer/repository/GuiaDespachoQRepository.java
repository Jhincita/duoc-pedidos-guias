package cl.duoc.ms_consumer.repository;

import cl.duoc.ms_consumer.model.GuiaDespachoQ;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuiaDespachoQRepository extends JpaRepository<GuiaDespachoQ, Long> {
}
