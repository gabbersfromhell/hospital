package com.toni.hospital.repositorio;

import com.toni.hospital.modelo.Planta;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlantaRepository extends JpaRepository<Planta, Long> {

    @Override
    @EntityGraph(attributePaths = "hospital")
    List<Planta> findAll();

    @Override
    @EntityGraph(attributePaths = "hospital")
    Optional<Planta> findById(Long id);

    boolean existsByHospital_IdAndNumeroPlanta(
            Long hospitalId,
            Integer numeroPlanta
    );

    boolean existsByHospital_IdAndNumeroPlantaAndIdNot(
            Long hospitalId,
            Integer numeroPlanta,
            Long plantaId
    );
}