package com.toni.hospital.repositorio;

import com.toni.hospital.modelo.ValoracionTriaje;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ValoracionTriajeRepository
        extends JpaRepository<ValoracionTriaje, Long> {

    /** Busca una valoración y carga su atención asociada. */
    @Override
    @EntityGraph(attributePaths = "atencionUrgencias")
    Optional<ValoracionTriaje> findById(Long id);

    /** Lista las valoraciones de una atención, de más reciente a más antigua. */
    @EntityGraph(attributePaths = "atencionUrgencias")
    List<ValoracionTriaje> findByAtencionUrgencias_IdOrderByFechaHoraDesc(
            Long atencionId
    );
}