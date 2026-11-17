package com.toni.hospital.repositorio;

import com.toni.hospital.modelo.Planta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlantaRepository extends JpaRepository<Planta, Long> {
}