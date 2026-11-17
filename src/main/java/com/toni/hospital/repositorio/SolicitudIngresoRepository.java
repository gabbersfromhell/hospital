package com.toni.hospital.repositorio;

import com.toni.hospital.modelo.SolicitudIngreso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolicitudIngresoRepository
        extends JpaRepository<SolicitudIngreso, Long> {
}