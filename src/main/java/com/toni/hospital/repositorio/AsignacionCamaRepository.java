package com.toni.hospital.repositorio;

import com.toni.hospital.modelo.AsignacionCama;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AsignacionCamaRepository
        extends JpaRepository<AsignacionCama, Long> {
}