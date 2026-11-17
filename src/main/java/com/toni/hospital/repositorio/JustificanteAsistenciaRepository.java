package com.toni.hospital.repositorio;

import com.toni.hospital.modelo.JustificanteAsistencia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JustificanteAsistenciaRepository
        extends JpaRepository<JustificanteAsistencia, Long> {
}