package com.toni.hospital.repositorio;

import com.toni.hospital.modelo.Interconsulta;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InterconsultaRepository
        extends JpaRepository<Interconsulta, Long> {

    @Override
    @EntityGraph(attributePaths = {"atencionUrgencias", "servicioDestino"})
    Optional<Interconsulta> findById(Long id);

    @EntityGraph(attributePaths = {"atencionUrgencias", "servicioDestino"})
    List<Interconsulta> findByAtencionUrgencias_IdOrderByFechaSolicitudDesc(
            Long atencionId
    );
}