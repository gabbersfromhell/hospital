package com.toni.hospital.repositorio;

import com.toni.hospital.modelo.Interconsulta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterconsultaRepository
        extends JpaRepository<Interconsulta, Long> {

    List<Interconsulta> findByAtencionUrgencias_IdOrderByFechaSolicitudDesc(
            Long atencionUrgenciasId
    );
}