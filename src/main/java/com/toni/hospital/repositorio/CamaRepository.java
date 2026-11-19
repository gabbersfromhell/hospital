package com.toni.hospital.repositorio;

import com.toni.hospital.modelo.Cama;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CamaRepository extends JpaRepository<Cama, Long> {

    @Override
    @EntityGraph(attributePaths = {"habitacion", "habitacion.planta"})
    List<Cama> findAll();

    @Override
    @EntityGraph(attributePaths = {"habitacion", "habitacion.planta"})
    Optional<Cama> findById(Long id);

    boolean existsByHabitacion_IdAndNumeroCama(
            Long habitacionId,
            Integer numeroCama
    );

    boolean existsByHabitacion_IdAndNumeroCamaAndIdNot(
            Long habitacionId,
            Integer numeroCama,
            Long camaId
    );

    boolean existsByHabitacion_Id(Long habitacionId);
}