package com.toni.hospital.repositorio;

import com.toni.hospital.modelo.Paciente;
import com.toni.hospital.modelo.TipoDocumento;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    @Override
    @EntityGraph(attributePaths = {"poblacion", "contactos"})
    List<Paciente> findAll();

    @Override
    @EntityGraph(attributePaths = {"poblacion", "contactos"})
    Optional<Paciente> findById(Long id);

    @EntityGraph(attributePaths = {"poblacion", "contactos"})
    Optional<Paciente> findByTipoDocumentoAndNumeroDocumento(
            TipoDocumento tipoDocumento,
            String numeroDocumento
    );

    boolean existsByPoblacion_Id(Long poblacionId);
}