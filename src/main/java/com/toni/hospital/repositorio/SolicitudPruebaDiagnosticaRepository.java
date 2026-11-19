package com.toni.hospital.repositorio;

import com.toni.hospital.modelo.SolicitudPruebaDiagnostica;
import com.toni.hospital.modelo.EstadoSolicitudPrueba;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Acceso a la base de datos para solicitudes de pruebas diagnósticas.
 */
public interface SolicitudPruebaDiagnosticaRepository
        extends JpaRepository<SolicitudPruebaDiagnostica, Long> {

    /** Busca una solicitud y carga sus relaciones para mostrarla. */
    @EntityGraph(attributePaths = {
            "servicioDestino",
            "atencionUrgencias",
            "ingresoHospitalario",
            "citaAmbulatoria"
    })
    Optional<SolicitudPruebaDiagnostica> findWithDetallesById(Long id);

    /** Lista todas las solicitudes, de la más reciente a la más antigua. */
    @EntityGraph(attributePaths = {
            "servicioDestino",
            "atencionUrgencias",
            "ingresoHospitalario",
            "citaAmbulatoria"
    })
    List<SolicitudPruebaDiagnostica>
    findAllByOrderByFechaHoraSolicitudDesc();

    /** Busca las solicitudes asociadas a una atención de urgencias. */
    @EntityGraph(attributePaths = "servicioDestino")
    List<SolicitudPruebaDiagnostica>
    findByAtencionUrgencias_IdOrderByFechaHoraSolicitudDesc(Long atencionUrgenciasId);

    /** Busca las solicitudes asociadas a un ingreso hospitalario. */
    @EntityGraph(attributePaths = "servicioDestino")
    List<SolicitudPruebaDiagnostica>
    findByIngresoHospitalario_IdOrderByFechaHoraSolicitudDesc(Long ingresoHospitalarioId);

    /** Busca las solicitudes asociadas a una cita ambulatoria. */
    @EntityGraph(attributePaths = "servicioDestino")
    List<SolicitudPruebaDiagnostica>
    findByCitaAmbulatoria_IdOrderByFechaHoraSolicitudDesc(Long citaAmbulatoriaId);

    /** Filtra las solicitudes por servicio destino y estado. */
    @EntityGraph(attributePaths = "servicioDestino")
    List<SolicitudPruebaDiagnostica>
    findByServicioDestino_IdAndEstadoOrderByFechaHoraSolicitudDesc(
            Long servicioDestinoId,
            EstadoSolicitudPrueba estado
    );
}