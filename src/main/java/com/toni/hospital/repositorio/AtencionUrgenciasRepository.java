package com.toni.hospital.repositorio;

import com.toni.hospital.modelo.AtencionUrgencias;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AtencionUrgenciasRepository
        extends JpaRepository<AtencionUrgencias, Long> {

    @Override
    @EntityGraph(attributePaths = {
            "paciente",
            "hospitalAtencion",
            "ubicacionActual"
    })
    List<AtencionUrgencias> findAll();

    @Override
    @EntityGraph(attributePaths = {
            "paciente",
            "hospitalAtencion",
            "ubicacionActual"
    })
    Optional<AtencionUrgencias> findById(Long id);

    /**
     * Censo de pacientes que siguen en Urgencias,
     * ordenado desde la llegada más antigua.
     */
    @EntityGraph(attributePaths = {
            "paciente",
            "hospitalAtencion",
            "ubicacionActual"
    })
    List<AtencionUrgencias>
    findByFechaHoraFinUrgenciasIsNullOrderByFechaHoraLlegadaAsc();
}