package com.toni.hospital.repositorio;

import com.toni.hospital.modelo.Habitacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HabitacionRepository
        extends JpaRepository<Habitacion, Long> {
}