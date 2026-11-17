package com.toni.hospital.repositorio;

import com.toni.hospital.modelo.TrasladoHospitalario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrasladoHospitalarioRepository
        extends JpaRepository<TrasladoHospitalario, Long> {
}