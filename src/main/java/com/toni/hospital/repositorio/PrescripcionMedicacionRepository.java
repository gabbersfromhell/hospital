package com.toni.hospital.repositorio;

import com.toni.hospital.modelo.EstadoPrescripcionMedicacion;
import com.toni.hospital.modelo.PrescripcionMedicacion;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/** Acceso a la base de datos para prescripciones de medicación. */
public interface PrescripcionMedicacionRepository
        extends JpaRepository<PrescripcionMedicacion, Long> {

    /** Busca una prescripción y carga sus datos relacionados. */
    @EntityGraph(attributePaths = {
            "medicamento",
            "medicoPrescriptor",
            "atencionUrgencias",
            "ingresoHospitalario",
            "citaAmbulatoria"
    })
    Optional<PrescripcionMedicacion> findWithDetallesById(Long id);

    /** Lista las prescripciones más recientes primero. */
    @EntityGraph(attributePaths = {"medicamento", "medicoPrescriptor"})
    List<PrescripcionMedicacion>
    findAllByOrderByFechaHoraPrescripcionDesc();

    /** Lista las pautas vinculadas a una atención de urgencias. */
    @EntityGraph(attributePaths = {"medicamento", "medicoPrescriptor"})
    List<PrescripcionMedicacion>
    findByAtencionUrgencias_IdOrderByFechaHoraPrescripcionDesc(
            Long atencionUrgenciasId
    );

    /** Lista las pautas vinculadas a un ingreso hospitalario. */
    @EntityGraph(attributePaths = {"medicamento", "medicoPrescriptor"})
    List<PrescripcionMedicacion>
    findByIngresoHospitalario_IdOrderByFechaHoraPrescripcionDesc(
            Long ingresoHospitalarioId
    );

    /** Lista las pautas vinculadas a una cita ambulatoria. */
    @EntityGraph(attributePaths = {"medicamento", "medicoPrescriptor"})
    List<PrescripcionMedicacion>
    findByCitaAmbulatoria_IdOrderByFechaHoraPrescripcionDesc(
            Long citaAmbulatoriaId
    );

    /** Busca las prescripciones de un medicamento según su estado. */
    @EntityGraph(attributePaths = {"medicamento", "medicoPrescriptor"})
    List<PrescripcionMedicacion>
    findByMedicamento_IdAndEstadoOrderByFechaHoraPrescripcionDesc(
            Long medicamentoId,
            EstadoPrescripcionMedicacion estado
    );
}