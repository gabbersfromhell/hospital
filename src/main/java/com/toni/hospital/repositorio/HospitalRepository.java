package com.toni.hospital.repositorio;

import com.toni.hospital.modelo.Hospital;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HospitalRepository extends JpaRepository<Hospital, Long> {

    @Override
    @EntityGraph(attributePaths = "servicios")
    List<Hospital> findAll();

    @Override
    @EntityGraph(attributePaths = "servicios")
    Optional<Hospital> findById(Long id);

    boolean existsByIdAndServicios_Id(Long hospitalId, Long servicioId);
}