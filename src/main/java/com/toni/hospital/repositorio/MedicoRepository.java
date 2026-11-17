package com.toni.hospital.repositorio;

import com.toni.hospital.modelo.Medico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicoRepository extends JpaRepository<Medico, Long> {
}