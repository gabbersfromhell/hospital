package com.toni.hospital.repositorio;

import com.toni.hospital.modelo.TrasladoHospitalario;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TrasladoHospitalarioRepository
        extends JpaRepository<TrasladoHospitalario, Long> {

    @Override
    @EntityGraph(attributePaths = {
            "atencionUrgencias",
            "hospitalDestino",
            "servicioRequerido"
    })
    Optional<TrasladoHospitalario> findById(Long id);

    @EntityGraph(attributePaths = {
            "atencionUrgencias",
            "hospitalDestino",
            "servicioRequerido"
    })
    List<TrasladoHospitalario>
    findByAtencionUrgencias_IdOrderByFechaHoraSolicitudDesc(
            Long atencionId
    );
}