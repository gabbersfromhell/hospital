package com.toni.hospital.repositorio;

import com.toni.hospital.modelo.SalaQuirurgica;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SalaQuirurgicaRepository
        extends JpaRepository<SalaQuirurgica, Long> {

    @EntityGraph(attributePaths = "hospital")
    List<SalaQuirurgica> findAllByOrderByCodigoAsc();

    @EntityGraph(attributePaths = "hospital")
    List<SalaQuirurgica> findByHospital_IdOrderByCodigoAsc(Long hospitalId);

    @EntityGraph(attributePaths = "hospital")
    Optional<SalaQuirurgica> findWithHospitalById(Long id);

    boolean existsByHospital_IdAndCodigoIgnoreCaseAndIdNot(
            Long hospitalId, String codigo, Long id
    );

    boolean existsByHospital_IdAndCodigoIgnoreCase(
            Long hospitalId, String codigo
    );
}
