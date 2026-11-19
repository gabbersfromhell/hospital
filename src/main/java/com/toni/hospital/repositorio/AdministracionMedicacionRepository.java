package com.toni.hospital.repositorio;

import com.toni.hospital.modelo.AdministracionMedicacion;
import com.toni.hospital.modelo.EstadoAdministracionMedicacion;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/** Acceso a la base de datos para administraciones de medicación. */
public interface AdministracionMedicacionRepository
        extends JpaRepository<AdministracionMedicacion, Long> {

    /** Busca una administración y carga sus datos relacionados. */
    @EntityGraph(attributePaths = {
            "prescripcionMedicacion",
            "prescripcionMedicacion.medicamento",
            "enfermeroResponsable"
    })
    Optional<AdministracionMedicacion> findWithDetallesById(Long id);

    /** Lista las administraciones de una prescripción por hora programada. */
    @EntityGraph(attributePaths = {
            "prescripcionMedicacion",
            "prescripcionMedicacion.medicamento",
            "enfermeroResponsable"
    })
    List<AdministracionMedicacion>
    findByPrescripcionMedicacion_IdOrderByFechaHoraProgramadaAsc(
            Long prescripcionId
    );

    /** Busca administraciones por estado y franja horaria programada. */
    @EntityGraph(attributePaths = {
            "prescripcionMedicacion",
            "prescripcionMedicacion.medicamento",
            "enfermeroResponsable"
    })
    List<AdministracionMedicacion>
    findByEstadoAndFechaHoraProgramadaBetweenOrderByFechaHoraProgramadaAsc(
            EstadoAdministracionMedicacion estado,
            LocalDateTime desde,
            LocalDateTime hasta
    );
}