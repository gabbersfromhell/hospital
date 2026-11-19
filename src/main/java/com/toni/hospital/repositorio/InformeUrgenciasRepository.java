package com.toni.hospital.repositorio;

import com.toni.hospital.modelo.InformeUrgencias;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InformeUrgenciasRepository
        extends JpaRepository<InformeUrgencias, Long> {

    @Override
    @EntityGraph(attributePaths = {"atencionUrgencias", "medicoResponsable"})
    Optional<InformeUrgencias> findById(Long id);

    @EntityGraph(attributePaths = {"atencionUrgencias", "medicoResponsable"})
    List<InformeUrgencias>
    findByAtencionUrgencias_IdOrderByFechaHoraEmisionDesc(Long atencionId);
}