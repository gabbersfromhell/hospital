package com.toni.hospital.repositorio;

import com.toni.hospital.modelo.ContactoPaciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactoPacienteRepository
        extends JpaRepository<ContactoPaciente, Long> {

    List<ContactoPaciente> findByPacienteId(Long pacienteId);
}