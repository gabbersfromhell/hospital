package com.toni.hospital.repositorio;

import com.toni.hospital.modelo.Paciente;
import com.toni.hospital.modelo.TipoDocumento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    Optional<Paciente> findByTipoDocumentoAndNumeroDocumento(
            TipoDocumento tipoDocumento,
            String numeroDocumento
    );
}