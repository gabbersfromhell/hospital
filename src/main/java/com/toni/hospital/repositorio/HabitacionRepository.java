package com.toni.hospital.repositorio;

import com.toni.hospital.modelo.Habitacion;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HabitacionRepository
        extends JpaRepository<Habitacion, Long> {

    @Override
    @EntityGraph(attributePaths = {"planta", "unidadHospitalaria"})
    List<Habitacion> findAll();

    @Override
    @EntityGraph(attributePaths = {"planta", "unidadHospitalaria"})
    Optional<Habitacion> findById(Long id);

    boolean existsByPlanta_IdAndNumeroHabitacion(
            Long plantaId,
            String numeroHabitacion
    );

    boolean existsByPlanta_IdAndNumeroHabitacionAndIdNot(
            Long plantaId,
            String numeroHabitacion,
            Long habitacionId
    );

    boolean existsByPlanta_Id(Long plantaId);
    boolean existsByUnidadHospitalaria_Id(Long unidadHospitalariaId);
}