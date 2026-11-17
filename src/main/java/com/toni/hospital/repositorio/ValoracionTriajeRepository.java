package com.toni.hospital.repositorio;

import com.toni.hospital.modelo.ValoracionTriaje;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ValoracionTriajeRepository
        extends JpaRepository<ValoracionTriaje, Long> {

    List<ValoracionTriaje> findByAtencionUrgencias_IdOrderByFechaHoraDesc(
            Long atencionUrgenciasId
    );
}