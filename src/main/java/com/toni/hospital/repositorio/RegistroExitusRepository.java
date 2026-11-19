package com.toni.hospital.repositorio;

import com.toni.hospital.modelo.RegistroExitus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RegistroExitusRepository
        extends JpaRepository<RegistroExitus, Long> {

    /*
     * Carga también las relaciones que necesita RegistroExitusResponse.
     * Así se pueden convertir a DTO al devolver la respuesta.
     */
    @Override
    @EntityGraph(attributePaths = {
            "paciente",
            "atencionUrgencias",
            "ingresoHospitalario",
            "medicoCertificador",
            "neveraMorgue"
    })
    List<RegistroExitus> findAll();

    @Override
    @EntityGraph(attributePaths = {
            "paciente",
            "atencionUrgencias",
            "ingresoHospitalario",
            "medicoCertificador",
            "neveraMorgue"
    })
    Optional<RegistroExitus> findById(Long id);

    /** Impide crear más de un registro para el mismo paciente. */
    boolean existsByPaciente_Id(Long pacienteId);

    /**
     * Comprueba si la nevera tiene un paciente que aún no ha sido retirado.
     */
    boolean existsByNeveraMorgue_IdAndFechaHoraRetiradaIsNull(
            Long neveraId
    );

    /**
     * Comprueba si otro registro ocupa la nevera,
     * excluyendo el registro que se está actualizando.
     */
    boolean existsByNeveraMorgue_IdAndFechaHoraRetiradaIsNullAndIdNot(
            Long neveraId,
            Long registroId
    );
}