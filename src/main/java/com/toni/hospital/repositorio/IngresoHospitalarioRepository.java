package com.toni.hospital.repositorio;

import com.toni.hospital.modelo.IngresoHospitalario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngresoHospitalarioRepository
        extends JpaRepository<IngresoHospitalario, Long> {
}