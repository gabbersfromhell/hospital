package com.toni.hospital.repositorio;

import com.toni.hospital.modelo.InformeUrgencias;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InformeUrgenciasRepository
        extends JpaRepository<InformeUrgencias, Long> {
}