package com.toni.hospital.repositorio;

import com.toni.hospital.modelo.ServicioHospitalario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServicioHospitalarioRepository
        extends JpaRepository<ServicioHospitalario, Long> {
}