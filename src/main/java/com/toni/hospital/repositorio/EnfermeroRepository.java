package com.toni.hospital.repositorio;

import com.toni.hospital.modelo.Enfermero;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnfermeroRepository
        extends JpaRepository<Enfermero, Long> {

    @Override
    @EntityGraph(attributePaths = "hospital")
    List<Enfermero> findAll();

    @Override
    @EntityGraph(attributePaths = "hospital")
    Optional<Enfermero> findById(Long id);

    boolean existsByNumeroColegiado(String numeroColegiado);

    boolean existsByNumeroColegiadoAndIdNot(
            String numeroColegiado,
            Long id
    );
}