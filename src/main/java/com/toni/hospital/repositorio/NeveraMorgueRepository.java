package com.toni.hospital.repositorio;

import com.toni.hospital.modelo.NeveraMorgue;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NeveraMorgueRepository
        extends JpaRepository<NeveraMorgue, Long> {

    @Override
    @EntityGraph(attributePaths = "hospital")
    List<NeveraMorgue> findAll();

    @Override
    @EntityGraph(attributePaths = "hospital")
    Optional<NeveraMorgue> findById(Long id);

    @EntityGraph(attributePaths = "hospital")
    List<NeveraMorgue> findByActivaTrue();

    boolean existsByHospital_IdAndCodigoAndIdNot(
            Long hospitalId,
            String codigo,
            Long id
    );

    boolean existsByHospital_IdAndCodigo(Long hospitalId, String codigo);
}