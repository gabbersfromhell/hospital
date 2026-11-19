package com.toni.hospital.repositorio;

import com.toni.hospital.modelo.CitaAmbulatoria;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Acceso a los datos de las citas ambulatorias.
 */
public interface CitaAmbulatoriaRepository
        extends JpaRepository<CitaAmbulatoria, Long> {

    /**
     * Busca una cita por ID y carga también los datos que necesitaremos
     * para mostrarla: paciente, hospital y servicio.
     */
    @EntityGraph(attributePaths = {"paciente", "hospital", "servicio"})
    Optional<CitaAmbulatoria> findWithDetallesById(Long id);

    /**
     * Lista el historial de citas de un paciente, de la más reciente
     * a la más antigua.
     */
    @EntityGraph(attributePaths = {"paciente", "hospital", "servicio"})
    List<CitaAmbulatoria> findByPaciente_IdOrderByFechaHoraProgramadaDesc(
            Long pacienteId
    );

    /**
     * Busca las citas de un servicio entre dos fechas,
     * en orden de hora programada.
     */
    @EntityGraph(attributePaths = {"paciente", "hospital", "servicio"})
    List<CitaAmbulatoria>
    findByServicio_IdAndFechaHoraProgramadaBetweenOrderByFechaHoraProgramadaAsc(
            Long servicioId,
            LocalDateTime desde,
            LocalDateTime hasta
    );

    @Override
    @EntityGraph(attributePaths = {"paciente", "hospital", "servicio"})
    List<CitaAmbulatoria> findAll();
}