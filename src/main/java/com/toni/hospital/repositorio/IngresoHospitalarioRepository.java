package com.toni.hospital.repositorio;

import com.toni.hospital.modelo.IngresoHospitalario;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IngresoHospitalarioRepository
        extends JpaRepository<IngresoHospitalario, Long> {

    @Override
    @EntityGraph(attributePaths = {
            "solicitudIngreso",
            "asignacionesCama.cama.habitacion.planta"
    })
    List<IngresoHospitalario> findAll();

    @Override
    @EntityGraph(attributePaths = {
            "solicitudIngreso",
            "asignacionesCama.cama.habitacion.planta"
    })
    Optional<IngresoHospitalario> findById(Long id);

    @EntityGraph(attributePaths = {
            "solicitudIngreso",
            "asignacionesCama.cama.habitacion.planta"
    })
    Optional<IngresoHospitalario> findBySolicitudIngreso_Id(Long solicitudId);
}