package com.toni.hospital.repositorio;

import com.toni.hospital.modelo.AtencionUrgencias;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AtencionUrgenciasRepository
        extends JpaRepository<AtencionUrgencias, Long> {
}