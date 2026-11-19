package com.toni.hospital.repositorio;

import com.toni.hospital.modelo.UnidadHospitalaria;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UnidadHospitalariaRepository
        extends JpaRepository<UnidadHospitalaria, Long> {

    @Override
    @EntityGraph(attributePaths = {"hospital", "servicioHospitalario"})
    List<UnidadHospitalaria> findAll();

    @Override
    @EntityGraph(attributePaths = {"hospital", "servicioHospitalario"})
    Optional<UnidadHospitalaria> findById(Long id);

    boolean existsByHospital_IdAndCodigo(Long hospitalId, String codigo);

    boolean existsByHospital_IdAndCodigoAndIdNot(
            Long hospitalId,
            String codigo,
            Long unidadId
    );
}