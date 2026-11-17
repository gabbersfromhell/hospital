package com.toni.hospital.repositorio;

import com.toni.hospital.modelo.Poblacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PoblacionRepository extends JpaRepository<Poblacion, Long> {
}