package com.toni.hospital.repositorio;

import com.toni.hospital.modelo.EstadoInformeRadiologico;
import com.toni.hospital.modelo.InformeRadiologico;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/** Acceso a la base de datos para informes radiológicos. */
public interface InformeRadiologicoRepository
        extends JpaRepository<InformeRadiologico, Long> {

    /** Busca el informe cargando la solicitud y el médico informante. */
    @EntityGraph(attributePaths = {"solicitudPrueba", "medicoInformante"})
    Optional<InformeRadiologico> findWithDetallesById(Long id);

    /** Lista los informes asociados a una solicitud. */
    @EntityGraph(attributePaths = "medicoInformante")
    List<InformeRadiologico>
    findBySolicitudPrueba_IdOrderByFechaHoraInformeDesc(Long solicitudId);

    /** Lista los informes elaborados por un médico. */
    @EntityGraph(attributePaths = "solicitudPrueba")
    List<InformeRadiologico>
    findByMedicoInformante_IdOrderByFechaHoraInformeDesc(Long medicoId);

    /** Lista los informes que tienen el estado indicado. */
    @EntityGraph(attributePaths = {"solicitudPrueba", "medicoInformante"})
    List<InformeRadiologico>
    findByEstadoOrderByFechaHoraInformeDesc(
            EstadoInformeRadiologico estado
    );
}