package com.toni.hospital.repositorio;

import com.toni.hospital.modelo.Medico;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MedicoRepository extends JpaRepository<Medico, Long> {

    @Override
    @EntityGraph(attributePaths = {"hospital", "servicioHospitalario"})
    List<Medico> findAll();

    @Override
    @EntityGraph(attributePaths = {"hospital", "servicioHospitalario"})
    Optional<Medico> findById(Long id);

    @EntityGraph(attributePaths = {"hospital", "servicioHospitalario"})
    List<Medico> findByActivoTrue();

    boolean existsByNumeroColegiado(String numeroColegiado);

    boolean existsByNumeroColegiadoAndIdNot(
            String numeroColegiado,
            Long medicoId
    );
}