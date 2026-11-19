package com.toni.hospital.repositorio;

import com.toni.hospital.modelo.UbicacionUrgencias;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UbicacionUrgenciasRepository
        extends JpaRepository<UbicacionUrgencias, Long> {

    @Override
    @EntityGraph(attributePaths = "hospital")
    List<UbicacionUrgencias> findAll();

    @Override
    @EntityGraph(attributePaths = "hospital")
    Optional<UbicacionUrgencias> findById(Long id);

    @EntityGraph(attributePaths = "hospital")
    List<UbicacionUrgencias> findByActivaTrueOrderByCodigoAsc();

    boolean existsByHospital_IdAndCodigo(Long hospitalId, String codigo);

    boolean existsByHospital_IdAndCodigoAndIdNot(
            Long hospitalId,
            String codigo,
            Long ubicacionId
    );
}