package com.toni.hospital.repositorio;

import com.toni.hospital.modelo.RegistroExitus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistroExitusRepository
        extends JpaRepository<RegistroExitus, Long> {
}