package com.toni.hospital.repositorio;

import com.toni.hospital.modelo.Cama;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CamaRepository extends JpaRepository<Cama, Long> {
}