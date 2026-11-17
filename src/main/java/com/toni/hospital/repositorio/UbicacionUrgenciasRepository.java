package com.toni.hospital.repositorio;

import com.toni.hospital.modelo.UbicacionUrgencias;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UbicacionUrgenciasRepository
        extends JpaRepository<UbicacionUrgencias, Long> {
}