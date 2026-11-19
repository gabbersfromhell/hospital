package com.toni.hospital.repositorio;

import com.toni.hospital.modelo.JustificanteAsistencia;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JustificanteAsistenciaRepository
        extends JpaRepository<JustificanteAsistencia, Long> {

    @Override
    @EntityGraph(attributePaths = {
            "atencionUrgencias",
            "atencionUrgencias.paciente"
    })
    Optional<JustificanteAsistencia> findById(Long id);

    @EntityGraph(attributePaths = {
            "atencionUrgencias",
            "atencionUrgencias.paciente"
    })
    List<JustificanteAsistencia>
    findByAtencionUrgencias_IdOrderByFechaHoraEmisionDesc(Long atencionId);
}