package com.toni.hospital.repositorio;

import com.toni.hospital.modelo.RestriccionConfidencialidad;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestriccionConfidencialidadRepository
        extends JpaRepository<RestriccionConfidencialidad, Long> {

    /**
     * Comprueba si hay una restricción activa para ese paciente
     * en ese hospital. Sin fecha de fin significa que sigue activa.
     */
    boolean existsByPaciente_IdAndHospital_IdAndFechaHoraFinIsNull(
            Long pacienteId,
            Long hospitalId
    );
}