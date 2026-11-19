package com.toni.hospital.repositorio;

import com.toni.hospital.modelo.EstadoResultadoLaboratorio;
import com.toni.hospital.modelo.ResultadoLaboratorio;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Acceso a la base de datos para resultados de laboratorio.
 */
public interface ResultadoLaboratorioRepository
        extends JpaRepository<ResultadoLaboratorio, Long> {

    /** Busca un resultado y carga también la solicitud a la que pertenece. */
    @EntityGraph(attributePaths = "solicitudPrueba")
    Optional<ResultadoLaboratorio> findWithSolicitudById(Long id);

    /** Lista los resultados de una solicitud, del más antiguo al más reciente. */
    List<ResultadoLaboratorio>
    findBySolicitudPrueba_IdOrderByFechaHoraResultadoAsc(Long solicitudId);

    /** Lista los resultados que tienen el estado indicado. */
    List<ResultadoLaboratorio>
    findByEstadoOrderByFechaHoraResultadoDesc(
            EstadoResultadoLaboratorio estado
    );
}