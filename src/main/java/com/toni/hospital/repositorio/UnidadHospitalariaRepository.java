package com.toni.hospital.repositorio;

import com.toni.hospital.modelo.UnidadHospitalaria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnidadHospitalariaRepository
        extends JpaRepository<UnidadHospitalaria, Long> {
}