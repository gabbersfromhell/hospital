package com.toni.hospital.repositorio;

import com.toni.hospital.modelo.SolicitudIngreso;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SolicitudIngresoRepository
        extends JpaRepository<SolicitudIngreso, Long> {

    @Override
    @EntityGraph(attributePaths = {
            "atencionUrgencias",
            "medicoSolicitante",
            "servicioHospitalario",
            "ingresoHospitalario"
    })
    Optional<SolicitudIngreso> findById(Long id);

    @EntityGraph(attributePaths = {
            "atencionUrgencias",
            "medicoSolicitante",
            "servicioHospitalario",
            "ingresoHospitalario"
    })
    List<SolicitudIngreso>
    findByAtencionUrgencias_IdOrderByFechaSolicitudDesc(Long atencionId);
}